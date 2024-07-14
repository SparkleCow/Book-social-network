package com.sparklecow.book.models;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account");
    private final String templateName;
    EmailTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
