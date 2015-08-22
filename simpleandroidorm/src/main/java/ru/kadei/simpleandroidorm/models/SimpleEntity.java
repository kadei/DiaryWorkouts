package ru.kadei.simpleandroidorm.models;

import ru.kadei.simpleandroidorm.annotations.Entity;
import ru.kadei.simpleandroidorm.annotations.Table;

/**
 * Created by kadei on 20.08.15.
 */
@Entity
@Table(name = "test")
public class SimpleEntity {

    public String name;
    public String description;
}
