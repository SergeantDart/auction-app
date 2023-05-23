package com.dumitrascuantonio.auctionapp.dto.response;

import com.dumitrascuantonio.auctionapp.entity.Image;
import com.dumitrascuantonio.auctionapp.entity.Lot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64String;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LotWithImageResponse {

    private Lot lot;

    private String image;

    public LotWithImageResponse(Lot lot, Image image) {
        this.lot = lot;
        setImage(image);
    }

    public void setImage(Image image) {
        if (image.getImageBytes().length != 0) {
            this.image = encodeBase64String(image.getImageBytes());
        } else {
            this.image = null;
        }
    }

}
