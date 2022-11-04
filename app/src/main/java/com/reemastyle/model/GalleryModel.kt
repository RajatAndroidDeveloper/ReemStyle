package com.reemastyle.model

import alirezat775.lib.carouselview.CarouselModel

class GalleryModel constructor(private var id: Int) : CarouselModel() {

    fun getId(): Int {
        return id
    }
}