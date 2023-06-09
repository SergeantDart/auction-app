package com.dumitrascuantonio.auctionapp.service;

import com.dumitrascuantonio.auctionapp.dto.response.LotWithImageResponse;
import com.dumitrascuantonio.auctionapp.entity.Image;
import com.dumitrascuantonio.auctionapp.entity.Lot;
import com.dumitrascuantonio.auctionapp.exception.ImageNotFoundException;
import com.dumitrascuantonio.auctionapp.repository.ImageRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private LotService lotService;


    public Image uploadImageToLot(MultipartFile file, Long lotId) throws IOException {

        Lot lot = lotService.getLotById(lotId);
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        if (file.getSize() != 0) {
            image.setImageBytes(compressBytes(file.getBytes()));
        } else {
            InputStream inputStream = getClass().getResourceAsStream("/images/image2.jpg");
            byte[] imageBytes = inputStream.readAllBytes();
            image.setImageBytes(compressBytes(imageBytes));
        }
        image.setLotId(lot.getId());

        return imageRepository.save(image);
    }

    public Image getImageToLot(Long lotId) throws IOException {
        Image image = imageRepository.findByLotId(lotId)
                .orElseThrow(() -> new ImageNotFoundException("Image cannot be found to lot with id: " + lotId));
        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(decompressBytes(image.getImageBytes()));
        }

        return image;
    }

    public List<LotWithImageResponse> getLotsWithImages(List<Lot> lots) {
        List<LotWithImageResponse> lotsWithImage = new ArrayList<>();
        for(Lot lot : lots) {
            try {
                Image image = getImageToLot(lot.getId());
                lotsWithImage.add(new LotWithImageResponse(lot, image));

            } catch (ImageNotFoundException | IOException e) {
                log.debug(e.getMessage());
                lotsWithImage.add(new LotWithImageResponse(lot, (String) null));
            }

        }
        return lotsWithImage;
    }

    private static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            log.error("Cannot compress bytes...");
        }
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            log.error("Cannot decompress bytes...");
        }
        return outputStream.toByteArray();
    }

}
