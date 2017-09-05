/*
 * stemgarden api
 * stemgarden api
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.stemweb.model;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

/**
 * LevelQuizQuestionlist
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-09-05T15:21:51.713+08:00")
public class LevelQuizQuestionlist {
  @SerializedName("question_id")
  private String questionId = null;

  @SerializedName("order")
  private String order = null;

  @SerializedName("questions")
  private String questions = null;

  @SerializedName("answers")
  private String answers = null;

  @SerializedName("title")
  private String title = null;

  @SerializedName("content")
  private String content = null;

  @SerializedName("app")
  private String app = null;

  public LevelQuizQuestionlist questionId(String questionId) {
    this.questionId = questionId;
    return this;
  }

   /**
   * Get questionId
   * @return questionId
  **/
  @ApiModelProperty(value = "")
  public String getQuestionId() {
    return questionId;
  }

  public void setQuestionId(String questionId) {
    this.questionId = questionId;
  }

  public LevelQuizQuestionlist order(String order) {
    this.order = order;
    return this;
  }

   /**
   * Get order
   * @return order
  **/
  @ApiModelProperty(value = "")
  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public LevelQuizQuestionlist questions(String questions) {
    this.questions = questions;
    return this;
  }

   /**
   * Get questions
   * @return questions
  **/
  @ApiModelProperty(value = "")
  public String getQuestions() {
    return questions;
  }

  public void setQuestions(String questions) {
    this.questions = questions;
  }

  public LevelQuizQuestionlist answers(String answers) {
    this.answers = answers;
    return this;
  }

   /**
   * Get answers
   * @return answers
  **/
  @ApiModelProperty(value = "")
  public String getAnswers() {
    return answers;
  }

  public void setAnswers(String answers) {
    this.answers = answers;
  }

  public LevelQuizQuestionlist title(String title) {
    this.title = title;
    return this;
  }

   /**
   * Get title
   * @return title
  **/
  @ApiModelProperty(value = "")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public LevelQuizQuestionlist content(String content) {
    this.content = content;
    return this;
  }

   /**
   * Get content
   * @return content
  **/
  @ApiModelProperty(value = "")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LevelQuizQuestionlist app(String app) {
    this.app = app;
    return this;
  }

   /**
   * Get app
   * @return app
  **/
  @ApiModelProperty(value = "")
  public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LevelQuizQuestionlist levelQuizQuestionlist = (LevelQuizQuestionlist) o;
    return Objects.equals(this.questionId, levelQuizQuestionlist.questionId) &&
        Objects.equals(this.order, levelQuizQuestionlist.order) &&
        Objects.equals(this.questions, levelQuizQuestionlist.questions) &&
        Objects.equals(this.answers, levelQuizQuestionlist.answers) &&
        Objects.equals(this.title, levelQuizQuestionlist.title) &&
        Objects.equals(this.content, levelQuizQuestionlist.content) &&
        Objects.equals(this.app, levelQuizQuestionlist.app);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questionId, order, questions, answers, title, content, app);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LevelQuizQuestionlist {\n");
    
    sb.append("    questionId: ").append(toIndentedString(questionId)).append("\n");
    sb.append("    order: ").append(toIndentedString(order)).append("\n");
    sb.append("    questions: ").append(toIndentedString(questions)).append("\n");
    sb.append("    answers: ").append(toIndentedString(answers)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    app: ").append(toIndentedString(app)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

