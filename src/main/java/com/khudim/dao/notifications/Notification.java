package com.khudim.dao.notifications;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Beaver.
 */
@Entity
public class Notification {

    public Notification() {
    }
    private Integer id;

    private String code;

    private String cron;

    private List<String> regions;

    private List<Integer> prices;

    private Long date;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public String getPerson() {
        return code;
    }

    public void setPerson(String code) {
        this.code = code;
    }
    @Column
    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
    @Column
    public List<String> getRegions() {
        return regions;
    }
    @Column
    public void setRegions(List<String> regions) {
        this.regions = regions;
    }
    @Column
    public List<Integer> getPrices() {
        return prices;
    }

    public void setPrices(List<Integer> prices) {
        this.prices = prices;
    }
    @Column
    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

}
