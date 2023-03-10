package com.sparta.hanghaebnb.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOUSE_ID",nullable = false)
    private House house;

    public Facility(String type) {
        this.type = type;
    }

    public void addHouse(House house){
        if( this.house != null){
            house.getFacilities().remove(this);
        }
        this.house = house;
        house.getFacilities().add(this);
    }
}
