package org.albumProjectFinal;

public class Data {

    private Integer id;
    private String albumId;
    private String albumName;
    private String artistName;
    private String albumType;
    private Integer albumStock;
    private Double albumPrice;
    private String albumStatus;
    private String country;

    public Data(Integer id,String albumId, String albumName, String artistName, String albumType,Integer albumStock, double albumPrice, String albumStatus, String country){
        this.id = id;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistName = artistName;
        this.albumType = albumType;
        this.albumStock = albumStock;
        this.albumPrice = albumPrice;
        this.albumStatus = albumStatus;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumType() {
        return albumType;
    }

    public Integer getAlbumStock() {
        return albumStock;
    }

    public Double getAlbumPrice() {
        return albumPrice;
    }

    public String getAlbumStatus() {
        return albumStatus;
    }

    public String getCountry() {
        return country;
    }

    public String getArtistName() {
        return artistName;
    }
}
