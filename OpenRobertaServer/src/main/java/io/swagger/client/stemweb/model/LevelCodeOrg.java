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
 * LevelCodeOrg
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-09-05T15:21:51.713+08:00")
public class LevelCodeOrg {
  @SerializedName("id")
  private UUID id = null;

  @SerializedName("level_name")
  private String levelName = null;

  @SerializedName("level_url")
  private String levelUrl = null;

  @SerializedName("game_name")
  private String gameName = null;

  @SerializedName("skin_name")
  private String skinName = null;

  @SerializedName("base_url")
  private String baseUrl = null;

  @SerializedName("app_name")
  private String appName = null;

  @SerializedName("level_properties")
  private String levelProperties = null;

  @SerializedName("script_id")
  private Long scriptId = null;

  @SerializedName("script_name")
  private String scriptName = null;

  @SerializedName("stage_position")
  private Long stagePosition = null;

  @SerializedName("level_position")
  private Long levelPosition = null;

  @SerializedName("has_contained_levels")
  private Boolean hasContainedLevels = null;

  @SerializedName("skip_sound")
  private Boolean skipSound = null;

  @SerializedName("skip_level")
  private Boolean skipLevel = null;

  @SerializedName("skip_dialog")
  private Boolean skipDialog = null;

  @SerializedName("pre_title")
  private String preTitle = null;

  @SerializedName("level_type")
  private String levelType = null;

  @SerializedName("script_src")
  private String scriptSrc = null;

  public LevelCodeOrg id(UUID id) {
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

  public LevelCodeOrg levelName(String levelName) {
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

  public LevelCodeOrg levelUrl(String levelUrl) {
    this.levelUrl = levelUrl;
    return this;
  }

   /**
   * Get levelUrl
   * @return levelUrl
  **/
  @ApiModelProperty(value = "")
  public String getLevelUrl() {
    return levelUrl;
  }

  public void setLevelUrl(String levelUrl) {
    this.levelUrl = levelUrl;
  }

  public LevelCodeOrg gameName(String gameName) {
    this.gameName = gameName;
    return this;
  }

   /**
   * Get gameName
   * @return gameName
  **/
  @ApiModelProperty(value = "")
  public String getGameName() {
    return gameName;
  }

  public void setGameName(String gameName) {
    this.gameName = gameName;
  }

  public LevelCodeOrg skinName(String skinName) {
    this.skinName = skinName;
    return this;
  }

   /**
   * Get skinName
   * @return skinName
  **/
  @ApiModelProperty(value = "")
  public String getSkinName() {
    return skinName;
  }

  public void setSkinName(String skinName) {
    this.skinName = skinName;
  }

  public LevelCodeOrg baseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }

   /**
   * Get baseUrl
   * @return baseUrl
  **/
  @ApiModelProperty(value = "")
  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public LevelCodeOrg appName(String appName) {
    this.appName = appName;
    return this;
  }

   /**
   * Get appName
   * @return appName
  **/
  @ApiModelProperty(value = "")
  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public LevelCodeOrg levelProperties(String levelProperties) {
    this.levelProperties = levelProperties;
    return this;
  }

   /**
   * Get levelProperties
   * @return levelProperties
  **/
  @ApiModelProperty(value = "")
  public String getLevelProperties() {
    return levelProperties;
  }

  public void setLevelProperties(String levelProperties) {
    this.levelProperties = levelProperties;
  }

  public LevelCodeOrg scriptId(Long scriptId) {
    this.scriptId = scriptId;
    return this;
  }

   /**
   * Get scriptId
   * @return scriptId
  **/
  @ApiModelProperty(value = "")
  public Long getScriptId() {
    return scriptId;
  }

  public void setScriptId(Long scriptId) {
    this.scriptId = scriptId;
  }

  public LevelCodeOrg scriptName(String scriptName) {
    this.scriptName = scriptName;
    return this;
  }

   /**
   * Get scriptName
   * @return scriptName
  **/
  @ApiModelProperty(value = "")
  public String getScriptName() {
    return scriptName;
  }

  public void setScriptName(String scriptName) {
    this.scriptName = scriptName;
  }

  public LevelCodeOrg stagePosition(Long stagePosition) {
    this.stagePosition = stagePosition;
    return this;
  }

   /**
   * Get stagePosition
   * @return stagePosition
  **/
  @ApiModelProperty(value = "")
  public Long getStagePosition() {
    return stagePosition;
  }

  public void setStagePosition(Long stagePosition) {
    this.stagePosition = stagePosition;
  }

  public LevelCodeOrg levelPosition(Long levelPosition) {
    this.levelPosition = levelPosition;
    return this;
  }

   /**
   * Get levelPosition
   * @return levelPosition
  **/
  @ApiModelProperty(value = "")
  public Long getLevelPosition() {
    return levelPosition;
  }

  public void setLevelPosition(Long levelPosition) {
    this.levelPosition = levelPosition;
  }

  public LevelCodeOrg hasContainedLevels(Boolean hasContainedLevels) {
    this.hasContainedLevels = hasContainedLevels;
    return this;
  }

   /**
   * Get hasContainedLevels
   * @return hasContainedLevels
  **/
  @ApiModelProperty(value = "")
  public Boolean getHasContainedLevels() {
    return hasContainedLevels;
  }

  public void setHasContainedLevels(Boolean hasContainedLevels) {
    this.hasContainedLevels = hasContainedLevels;
  }

  public LevelCodeOrg skipSound(Boolean skipSound) {
    this.skipSound = skipSound;
    return this;
  }

   /**
   * Get skipSound
   * @return skipSound
  **/
  @ApiModelProperty(value = "")
  public Boolean getSkipSound() {
    return skipSound;
  }

  public void setSkipSound(Boolean skipSound) {
    this.skipSound = skipSound;
  }

  public LevelCodeOrg skipLevel(Boolean skipLevel) {
    this.skipLevel = skipLevel;
    return this;
  }

   /**
   * Get skipLevel
   * @return skipLevel
  **/
  @ApiModelProperty(value = "")
  public Boolean getSkipLevel() {
    return skipLevel;
  }

  public void setSkipLevel(Boolean skipLevel) {
    this.skipLevel = skipLevel;
  }

  public LevelCodeOrg skipDialog(Boolean skipDialog) {
    this.skipDialog = skipDialog;
    return this;
  }

   /**
   * Get skipDialog
   * @return skipDialog
  **/
  @ApiModelProperty(value = "")
  public Boolean getSkipDialog() {
    return skipDialog;
  }

  public void setSkipDialog(Boolean skipDialog) {
    this.skipDialog = skipDialog;
  }

  public LevelCodeOrg preTitle(String preTitle) {
    this.preTitle = preTitle;
    return this;
  }

   /**
   * Get preTitle
   * @return preTitle
  **/
  @ApiModelProperty(value = "")
  public String getPreTitle() {
    return preTitle;
  }

  public void setPreTitle(String preTitle) {
    this.preTitle = preTitle;
  }

  public LevelCodeOrg levelType(String levelType) {
    this.levelType = levelType;
    return this;
  }

   /**
   * Get levelType
   * @return levelType
  **/
  @ApiModelProperty(value = "")
  public String getLevelType() {
    return levelType;
  }

  public void setLevelType(String levelType) {
    this.levelType = levelType;
  }

  public LevelCodeOrg scriptSrc(String scriptSrc) {
    this.scriptSrc = scriptSrc;
    return this;
  }

   /**
   * Get scriptSrc
   * @return scriptSrc
  **/
  @ApiModelProperty(value = "")
  public String getScriptSrc() {
    return scriptSrc;
  }

  public void setScriptSrc(String scriptSrc) {
    this.scriptSrc = scriptSrc;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LevelCodeOrg levelCodeOrg = (LevelCodeOrg) o;
    return Objects.equals(this.id, levelCodeOrg.id) &&
        Objects.equals(this.levelName, levelCodeOrg.levelName) &&
        Objects.equals(this.levelUrl, levelCodeOrg.levelUrl) &&
        Objects.equals(this.gameName, levelCodeOrg.gameName) &&
        Objects.equals(this.skinName, levelCodeOrg.skinName) &&
        Objects.equals(this.baseUrl, levelCodeOrg.baseUrl) &&
        Objects.equals(this.appName, levelCodeOrg.appName) &&
        Objects.equals(this.levelProperties, levelCodeOrg.levelProperties) &&
        Objects.equals(this.scriptId, levelCodeOrg.scriptId) &&
        Objects.equals(this.scriptName, levelCodeOrg.scriptName) &&
        Objects.equals(this.stagePosition, levelCodeOrg.stagePosition) &&
        Objects.equals(this.levelPosition, levelCodeOrg.levelPosition) &&
        Objects.equals(this.hasContainedLevels, levelCodeOrg.hasContainedLevels) &&
        Objects.equals(this.skipSound, levelCodeOrg.skipSound) &&
        Objects.equals(this.skipLevel, levelCodeOrg.skipLevel) &&
        Objects.equals(this.skipDialog, levelCodeOrg.skipDialog) &&
        Objects.equals(this.preTitle, levelCodeOrg.preTitle) &&
        Objects.equals(this.levelType, levelCodeOrg.levelType) &&
        Objects.equals(this.scriptSrc, levelCodeOrg.scriptSrc);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, levelName, levelUrl, gameName, skinName, baseUrl, appName, levelProperties, scriptId, scriptName, stagePosition, levelPosition, hasContainedLevels, skipSound, skipLevel, skipDialog, preTitle, levelType, scriptSrc);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LevelCodeOrg {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    levelName: ").append(toIndentedString(levelName)).append("\n");
    sb.append("    levelUrl: ").append(toIndentedString(levelUrl)).append("\n");
    sb.append("    gameName: ").append(toIndentedString(gameName)).append("\n");
    sb.append("    skinName: ").append(toIndentedString(skinName)).append("\n");
    sb.append("    baseUrl: ").append(toIndentedString(baseUrl)).append("\n");
    sb.append("    appName: ").append(toIndentedString(appName)).append("\n");
    sb.append("    levelProperties: ").append(toIndentedString(levelProperties)).append("\n");
    sb.append("    scriptId: ").append(toIndentedString(scriptId)).append("\n");
    sb.append("    scriptName: ").append(toIndentedString(scriptName)).append("\n");
    sb.append("    stagePosition: ").append(toIndentedString(stagePosition)).append("\n");
    sb.append("    levelPosition: ").append(toIndentedString(levelPosition)).append("\n");
    sb.append("    hasContainedLevels: ").append(toIndentedString(hasContainedLevels)).append("\n");
    sb.append("    skipSound: ").append(toIndentedString(skipSound)).append("\n");
    sb.append("    skipLevel: ").append(toIndentedString(skipLevel)).append("\n");
    sb.append("    skipDialog: ").append(toIndentedString(skipDialog)).append("\n");
    sb.append("    preTitle: ").append(toIndentedString(preTitle)).append("\n");
    sb.append("    levelType: ").append(toIndentedString(levelType)).append("\n");
    sb.append("    scriptSrc: ").append(toIndentedString(scriptSrc)).append("\n");
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
