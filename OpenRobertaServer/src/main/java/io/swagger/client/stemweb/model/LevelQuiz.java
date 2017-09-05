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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * LevelQuiz
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-09-05T15:21:51.713+08:00")
public class LevelQuiz {
  @SerializedName("id")
  private UUID id = null;

  @SerializedName("level_name")
  private String levelName = null;

  @SerializedName("questionlist")
  private List<LevelQuizQuestionlist> questionlist = null;

  public LevelQuiz id(UUID id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public LevelQuiz levelName(String levelName) {
    this.levelName = levelName;
    return this;
  }

   /**
   * Get levelName
   * @return levelName
  **/
  @ApiModelProperty(value = "")
  public String getLevelName() {
    return levelName;
  }

  public void setLevelName(String levelName) {
    this.levelName = levelName;
  }

  public LevelQuiz questionlist(List<LevelQuizQuestionlist> questionlist) {
    this.questionlist = questionlist;
    return this;
  }

  public LevelQuiz addQuestionlistItem(LevelQuizQuestionlist questionlistItem) {
    if (this.questionlist == null) {
      this.questionlist = new ArrayList<LevelQuizQuestionlist>();
    }
    this.questionlist.add(questionlistItem);
    return this;
  }

   /**
   * Get questionlist
   * @return questionlist
  **/
  @ApiModelProperty(value = "")
  public List<LevelQuizQuestionlist> getQuestionlist() {
    return questionlist;
  }

  public void setQuestionlist(List<LevelQuizQuestionlist> questionlist) {
    this.questionlist = questionlist;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LevelQuiz levelQuiz = (LevelQuiz) o;
    return Objects.equals(this.id, levelQuiz.id) &&
        Objects.equals(this.levelName, levelQuiz.levelName) &&
        Objects.equals(this.questionlist, levelQuiz.questionlist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, levelName, questionlist);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LevelQuiz {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    levelName: ").append(toIndentedString(levelName)).append("\n");
    sb.append("    questionlist: ").append(toIndentedString(questionlist)).append("\n");
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

