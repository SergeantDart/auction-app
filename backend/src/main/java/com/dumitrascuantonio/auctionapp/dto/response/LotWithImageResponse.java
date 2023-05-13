package com.dumitrascuantonio.auctionapp.dto.response;

import com.dumitrascuantonio.auctionapp.entity.Image;
import com.dumitrascuantonio.auctionapp.entity.Lot;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
            this.image = Base64.getEncoder().encodeToString(image.getImageBytes());
        } else {
            this.image = null;
        }
    }

}
