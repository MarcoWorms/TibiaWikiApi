package com.tibiawiki.domain.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tibiawiki.domain.enums.BuildingType;
import com.tibiawiki.domain.enums.City;
import com.tibiawiki.domain.enums.YesNo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties({"objectType"})
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Building extends WikiObject {

    private BuildingType type;
    private String location;
    private String posx;
    private String posy;
    private String posz;
    private String street;
    private String street2;
    private String street3;
    private String street4;
    private String street5;
    private Integer houseid;
    private Integer size;
    private Integer beds;
    private Integer rent;
    private YesNo ownable;
    private City city;
    private Integer openwindows;
    private Integer floors;
    private Integer rooms;
    private String furnishings;
    private String image;

    @Override
    public List<String> fieldOrder() {
        return Arrays.asList("name", "implemented", "type", "location", "posx", "posy", "posz", "street", "street2",
                "street3", "street4", "street5", "houseid", "size", "beds", "rent", "ownable", "city", "openwindows",
                "floors", "rooms", "furnishings", "notes", "history", "image", "status");
    }
}