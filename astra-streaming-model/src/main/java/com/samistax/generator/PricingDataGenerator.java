package com.samistax.generator;

import com.samistax.dto.PriceUpdateEvent;

import java.util.Random;

public class PricingDataGenerator {
    public static PriceUpdateEvent generateData() {
        Random random = new Random();

        PriceUpdateEvent priceUpdateEvent = new PriceUpdateEvent();

        // Generate random values for each property
        priceUpdateEvent.setItemId("SKU_" + (random.nextInt(90000) + 10000)); // SKU ID in range (10000-99999)
        priceUpdateEvent.setItemName("Product_" + (char) (random.nextInt(26) + 'A')); // Random Product Name (A-Z suffix)
        priceUpdateEvent.setAffiliation("Affiliate_" + random.nextInt(50)); // Random affiliation value
        priceUpdateEvent.setCoupon("DISCOUNT_" + random.nextInt(9999)); // Random coupon (e.g., DISCOUNT_1234)
        priceUpdateEvent.setDiscount(random.nextDouble() * 100); // Random discount (up to 100)
        priceUpdateEvent.setIdx(random.nextInt(100) + 1); // Random index (1 to 100)
        priceUpdateEvent.setItemBrand("Brand_" + (char) (random.nextInt(26) + 'A')); // Random Brand (A-Z)
        priceUpdateEvent.setItemCategory("Category_" + random.nextInt(10)); // Top-level category
        priceUpdateEvent.setItemCategory2("Category2_" + random.nextInt(10)); // Secondary category
        priceUpdateEvent.setItemCategory3("Category3_" + random.nextInt(10)); // 3rd-level category
        priceUpdateEvent.setItemCategory4("Category4_" + random.nextInt(10)); // 4th-level category
        priceUpdateEvent.setItemCategory5("Category5_" + random.nextInt(10)); // 5th-level category
        priceUpdateEvent.setItemListId("List_" + random.nextInt(100)); // Random list ID
        priceUpdateEvent.setItemListName("ListName_" + random.nextInt(10)); // Random list name
        priceUpdateEvent.setItemVariant("Variant_" + (char) (random.nextInt(26) + 'A')); // Random variant (e.g., "Variant_A")
        priceUpdateEvent.setLocationId("LOC_" + random.nextInt(9999)); // Random location ID
        priceUpdateEvent.setPrice(Math.round((random.nextDouble() * 100 + 1) * 100.0) / 100.0); // Random price in range (1.00 - 100.00), 2 decimal places
        priceUpdateEvent.setQuantity(random.nextInt(20) + 1); // Random quantity (1 to 20)

        return priceUpdateEvent;
    }
}