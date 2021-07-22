package ru.gb_dz2.utils;

import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;
import ru.gb_dz2.dto.Product;

@Slf4j
public class CommonLog {
    public static void logGetBasic(Response<Product> response) {
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
    }

    public static void logGetFull(Response<Product> response) {
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
    }

}
