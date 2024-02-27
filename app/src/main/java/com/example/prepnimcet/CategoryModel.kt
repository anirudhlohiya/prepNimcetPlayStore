package com.example.prepnimcet

public class CategoryModel {

    var categoryId: String? = null
    var QuizTitle: String? = null
    var QuizDescription: String? = null

    constructor(categoryId: String?, QuizTitle: String?, QuizDescription: String?) {
        this.categoryId = categoryId
        this.QuizTitle = QuizTitle
        this.QuizDescription = QuizDescription
    }



    constructor()
}

//class CategoryModel(
//    var categoryId: String,
//    var quizDescription: String,
//    var quizTitle: String
//) {
//    constructor() : this("", "", "")
//}
