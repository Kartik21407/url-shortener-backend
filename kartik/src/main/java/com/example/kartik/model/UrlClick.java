package com.example.kartik.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "url_clicks")
public class UrlClick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_mapping_id", nullable = false)
    private UrlMapping urlMapping;

    @Column(nullable = false)
    private LocalDateTime clickTime;

    // Default constructor
    public UrlClick() {}

    public UrlClick(UrlMapping urlMapping, LocalDateTime clickTime) {
        this.urlMapping = urlMapping;
        this.clickTime = clickTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UrlMapping getUrlMapping() { return urlMapping; }
    public void setUrlMapping(UrlMapping urlMapping) { this.urlMapping = urlMapping; }

    public LocalDateTime getClickTime() { return clickTime; }
    public void setClickTime(LocalDateTime clickTime) { this.clickTime = clickTime; }
}