package com.reemastyle.model

import alirezat775.lib.carouselview.CarouselModel

class EmptyGalleryModel constructor(private val text: String) : CarouselModel() {

    fun getText(): String {
        return text
    }
}