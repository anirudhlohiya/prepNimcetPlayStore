package com.example.prepnimcet

class Questions (
    var optionD: String = "", var optionC: String = "", var optionB: String = "",
    var question: String = "", var optionA: String = "", var answer: String = ""
) {
    override fun toString(): String {
        return "Questions(optionD='$optionD', optionC='$optionC', optionB='$optionB', question='$question', optionA='$optionA', answer='$answer')"
    }

}