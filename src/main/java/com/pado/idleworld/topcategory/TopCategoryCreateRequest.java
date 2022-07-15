package com.pado.idleworld.topcategory;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopCategoryCreateRequest {

    private String title;
    @Lob
    private String imageUrl;
}
