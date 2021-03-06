package com.tibiawiki.serviceinterface;

import com.tibiawiki.domain.objects.Outfit;
import com.tibiawiki.domain.objects.validation.ValidationException;
import com.tibiawiki.process.ModifyAny;
import com.tibiawiki.process.RetrieveOutfits;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Api(value = "Outfits")
@Path("/outfits")
public class OutfitsResource {

    private RetrieveOutfits retrieveOutfits;
    private ModifyAny modifyAny;

    @Autowired
    private OutfitsResource(RetrieveOutfits retrieveOutfits, ModifyAny modifyAny) {
        this.retrieveOutfits = retrieveOutfits;
        this.modifyAny = modifyAny;
    }

    @GET
    @ApiOperation(value = "Get a list of outfits")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "list of outfits retrieved")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOutfits(@ApiParam(value = "optionally expands the result to retrieve not only " +
            "the outfit names but the full outfits", required = false)
                               @QueryParam("expand") Boolean expand) {
        return Response.ok()
                .entity(expand != null && expand
                        ? retrieveOutfits.getOutfitsJSON().map(JSONObject::toMap)
                        : retrieveOutfits.getOutfitsList()
                )
                .build();
    }

    @GET
    @Path("/{name}")
    @ApiOperation(value = "Get a specific outfit by name")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOutfitsByName(@PathParam("name") String name) {
        return retrieveOutfits.getOutfitJSON(name)
                .map(a -> Response.ok()
                        .entity(a.toString(2))
                        .build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .build());
    }

    @PUT
    @ApiOperation(value = "Modify an outfit")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "the changed outfit"),
            @ApiResponse(code = 400, message = "the provided changed outfit is not valid"),
            @ApiResponse(code = 401, message = "not authorized to edit without providing credentials")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putOutfit(Outfit outfit, @HeaderParam("X-WIKI-Edit-Summary") String editSummary) {
        return modifyAny.modify(outfit, editSummary)
                .map(a -> Response.ok()
                        .entity(a)
                        .build())
                .recover(ValidationException.class, e -> Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build())
                .recover(e -> Response.serverError().build())
                .get();
    }
}
