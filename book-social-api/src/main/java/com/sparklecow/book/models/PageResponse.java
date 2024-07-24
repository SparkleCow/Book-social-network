package com.sparklecow.book.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> implements Serializable {

    List<T> content;
    int page;
    int size;
    int totalPages;
    long totalElements;
    boolean first;
    boolean last;
}
