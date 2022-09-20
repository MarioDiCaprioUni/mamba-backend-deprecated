package com.mariodicaprio.mamba.responses;


import lombok.Data;
import org.springframework.data.domain.Page;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class PageResponse<T> {

    @Min(0)
    @NotNull
    private final int totalPages;

    @Min(1)
    @NotNull
    private final int index;

    @Min(0)
    @NotNull
    private final int size;

    @NotNull
    private final List<T> content;

    public PageResponse(Page<T> page) {
        this.totalPages = page.getTotalPages();
        this.index = page.getNumber() + 1;
        this.size = page.getSize();
        this.content = page.getContent();
    }

}
