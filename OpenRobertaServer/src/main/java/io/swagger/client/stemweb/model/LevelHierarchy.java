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

import java.util.UUID;

/**
 * LevelHierarchy
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-09-05T15:21:51.713+08:00")
public class LevelHierarchy {
  @SerializedName("lessonId")
  private UUID lessonId = null;

  @SerializedName("stageId")
  private UUID stageId = null;

  @SerializedName("subStageId")
  private UUID subStageId = null;

  @SerializedName("levelId")
  private UUID levelId = null;

  public LevelHierarchy lessonId(UUID lessonId) {
    this.lessonId = lessonId;
    return this;
  }

   /**
   * Get lessonId
   * @return lessonId
  **/
  @ApiModelProperty(value = "")
  public UUID getLessonId() {
    return lessonId;
  }

  public void setLessonId(UUID lessonId) {
    this.lessonId = lessonId;
  }

  public LevelHierarchy stageId(UUID stageId) {
    this.stageId = stageId;
    return this;
  }

   /**
   * Get stageId
   * @return stageId
  **/
  @ApiModelProperty(value = "")
  public UUID getStageId() {
    return stageId;
  }

  public void setStageId(UUID stageId) {
    this.stageId = stageId;
  }

  public LevelHierarchy subStageId(UUID subStageId) {
    this.subStageId = subStageId;
    return this;
  }

   /**
   * Get subStageId
   * @return subStageId
  **/
  @ApiModelProperty(value = "")
  public UUID getSubStageId() {
    return subStageId;
  }

  public void setSubStageId(UUID subStageId) {
    this.subStageId = subStageId;
  }

  public LevelHierarchy levelId(UUID levelId) {
    this.levelId = levelId;
    return this;
  }

   /**
   * Get levelId
   * @return levelId
  **/
  @ApiModelProperty(value = "")
  public UUID getLevelId() {
    return levelId;
  }

  public void setLevelId(UUID levelId) {
    this.levelId = levelId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LevelHierarchy levelHierarchy = (LevelHierarchy) o;
    return Objects.equals(this.lessonId, levelHierarchy.lessonId) &&
        Objects.equals(this.stageId, levelHierarchy.stageId) &&
        Objects.equals(this.subStageId, levelHierarchy.subStageId) &&
        Objects.equals(this.levelId, levelHierarchy.levelId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lessonId, stageId, subStageId, levelId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LevelHierarchy {\n");
    
    sb.append("    lessonId: ").append(toIndentedString(lessonId)).append("\n");
    sb.append("    stageId: ").append(toIndentedString(stageId)).append("\n");
    sb.append("    subStageId: ").append(toIndentedString(subStageId)).append("\n");
    sb.append("    levelId: ").append(toIndentedString(levelId)).append("\n");
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
