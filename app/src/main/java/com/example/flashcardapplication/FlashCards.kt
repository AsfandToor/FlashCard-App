package com.example.flashcardapplication

class FlashCards(
    val id: String,
    val question: String,
    val answer: String
) {
    constructor() : this("", "", "")
}