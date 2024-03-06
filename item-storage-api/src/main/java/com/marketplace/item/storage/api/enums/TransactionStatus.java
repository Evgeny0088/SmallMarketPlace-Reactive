package com.marketplace.item.storage.api.enums;

public enum TransactionStatus {

    OK(200), BAD_REQUEST(400), INTERNAL_ERROR(500);

    TransactionStatus(int value) {
        this.value = value;
    }

    private final int value;
}
