package com.example.prepnimcet

data class MockTestQuestionData(
    var question: String? = null,
    var optionA: String? = null,
    var optionB: String? = null,
    var optionC: String? = null,
    var optionD: String? = null,
    var answer: String? = null,
    var imageString: String? = null,
    var userAnswer: String? = null,
    var isVisited: Boolean = false
)
