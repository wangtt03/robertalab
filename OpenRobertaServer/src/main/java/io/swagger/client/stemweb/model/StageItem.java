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
 * StageItem
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-09-05T15:21:51.713+08:00")
public class StageItem {
  @SerializedName("id")
  private UUID id = null;

  @SerializedName("stageName")
  private String stageName = null;

  @SerializedName("stageOrder")
  private Integer stageOrder = null;

  @SerializedName("substageOrder")
  private Integer substageOrder = null;

  @SerializedName("subStages")
  private List<StageItem> subStages = null;

  @SerializedName("levels")
  private List<LevelItem> levels = null;

  public StageItem id(UUID id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(example = "d290f1ee-6c54-4b01-90e6-d701748f0851", value = "")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public StageItem stageName(String stageName) {
    this.stageName = stageName;
    return this;
  }

   /**
   * Get stageName
   * @return stageName
  **/
  @ApiModelProperty(example = "stageFirst", value = "")
  public String getStageName() {
    return stageName;
  }

  public void setStageName(String stageName) {
    this.stageName = stageName;
  }

  public StageItem stageOrder(Integer stageOrder) {
    this.stageOrder = stageOrder;
    return this;
  }

   /**
   * Get stageOrder
   * @return stageOrder
  **/
  @ApiModelProperty(value = "")
  public Integer getStageOrder() {
    return stageOrder;
  }

  public void setStageOrder(Integer stageOrder) {
    this.stageOrder = stageOrder;
  }

  public StageItem substageOrder(Integer substageOrder) {
    this.substageOrder = substageOrder;
    return this;
  }

   /**
   * Get substageOrder
   * @return substageOrder
  **/
  @ApiModelProperty(value = "")
  public Integer getSubstageOrder() {
    return substageOrder;
  }

  public void setSubstageOrder(Integer substageOrder) {
    this.substageOrder = substageOrder;
  }

  public StageItem subStages(List<StageItem> subStages) {
    this.subStages = subStages;
    return this;
  }

  public StageItem addSubStagesItem(StageItem subStagesItem) {
    if (this.subStages == null) {
      this.subStages = new ArrayList<StageItem>();
    }
    this.subStages.add(subStagesItem);
    return this;
  }

   /**
   * Get subStages
   * @return subStages
  **/
  @ApiModelProperty(value = "")
  public List<StageItem> getSubStages() {
    return subStages;
  }

  public void setSubStages(List<StageItem> subStages) {
    this.subStages = subStages;
  }

  public StageItem levels(List<LevelItem> levels) {
    this.levels = levels;
    return this;
  }

  public StageItem addLevelsItem(LevelItem levelsItem) {
    if (this.levels == null) {
      this.levels = new ArrayList<LevelItem>();
    }
    this.levels.add(levelsItem);
    return this;
  }

   /**
   * Get levels
   * @return levels
  **/
  @ApiModelProperty(value = "")
  public List<LevelItem> getLevels() {
    return levels;
  }

  public void setLevels(List<LevelItem> levels) {
    this.levels = levels;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StageItem stageItem = (StageItem) o;
    return Objects.equals(this.id, stageItem.id) &&
        Objects.equals(this.stageName, stageItem.stageName) &&
        Objects.equals(this.stageOrder, stageItem.stageOrder) &&
        Objects.equals(this.substageOrder, stageItem.substageOrder) &&
        Objects.equals(this.subStages, stageItem.subStages) &&
        Objects.equals(this.levels, stageItem.levels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, stageName, stageOrder, substageOrder, subStages, levels);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StageItem {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    stageName: ").append(toIndentedString(stageName)).append("\n");
    sb.append("    stageOrder: ").append(toIndentedString(stageOrder)).append("\n");
    sb.append("    substageOrder: ").append(toIndentedString(substageOrder)).append("\n");
    sb.append("    subStages: ").append(toIndentedString(subStages)).append("\n");
    sb.append("    levels: ").append(toIndentedString(levels)).append("\n");
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
