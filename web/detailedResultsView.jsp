
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="model.Results"%>




<!doctype html>


<html>

    <head>
        <meta charset="UTF-8">
        <title>Edit Profile</title>
        <link rel="stylesheet" href="css/form.css">
        <meta name="viewport" content="width=device-width , initial-scale=1.0">
    </head>

    <body>

        <div class="login-page">
            <div class="logo">               
                <img class="logohome" src="img/logonew3.png" alt="LIT Gallery Logo">
            </div>
            <h1>Edit Question</h1>
            <div class="form">
                <form action="ResultsController" method="get" class="show-form" name="results">


                    <br>
                    <label>category</label>
                    <input type="text" name="category" id="category" value="${results.category}"/>
                    <br>
                    <label>type</label>
                    <input type="text" name="type" id="type" value="${results.type}"/>
                    <br>
                    <label>difficulty</label>
                    <input type="text" name="difficulty" id="difficulty" value="${results.difficulty}"/>
                    <br>
                    <label>question</label>
                    <input type="text" name="question" id="question" value="${results.question}"/>
                    <br>
                    <label>correct answer</label>
                    <input type="text" name="correct_answer" id="correct_answer" value="${results.correct_answer}"/>
                    <br>
                    <label>incorrect answers</label>
                    <input type="text" name="incorrect_answers1" id="incorrect_answers1" value="${results.incorrect_answers1}"/>
                    <br>


                    <input type="submit" name="menu" value="Update" />


                </form>
            </div>
        </div>


        <script
            src="https://code.jquery.com/jquery-1.12.4.js"
            integrity="sha256-Qw82+bXyGq6MydymqBxNPYTaUXXq7c8v3CwiYwLLNXU="
        crossorigin="anonymous"></script>
        <script src="js/jquery.validate.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/form-validation.js"></script>
    </body>
</html>


