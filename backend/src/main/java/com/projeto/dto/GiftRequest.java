package com.projeto.dto;

import com.projeto.model.GiftStatus;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GiftRequest {

    @NotBlank
    private String name;

    private String purchaseLink;

    private String imageUrl;

    private Boolean visible;

    @JsonProperty("category")
    private String category;

    private GiftStatus status;

    public GiftRequest() {
        System.out.println("DEBUG: GiftRequest NO-ARGS constructor called");
    }

    public GiftRequest(String name, String purchaseLink, String imageUrl, Boolean visible, String category,
            GiftStatus status) {
        System.out.println("DEBUG: GiftRequest ALL-ARGS constructor called. Category=" + category);
        this.name = name;
        this.purchaseLink = purchaseLink;
        this.imageUrl = imageUrl;
        this.visible = visible;
        this.category = category;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurchaseLink() {
        return purchaseLink;
    }

    public void setPurchaseLink(String purchaseLink) {
        this.purchaseLink = purchaseLink;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        System.out.println("DEBUG: GiftRequest.setCategory called with: " + category);
        this.category = category;
    }

    public GiftStatus getStatus() {
        return status;
    }

    public void setStatus(GiftStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GiftRequest{" +
                "name='" + name + '\'' +
                ", purchaseLink='" + purchaseLink + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", visible=" + visible +
                ", category='" + category + '\'' +
                ", status=" + status +
                '}';
    }
}