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
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.joda.time.DateTime;

/**
 * Classroom
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-08-30T00:47:33.626+08:00")
public class Classroom {
  @SerializedName("id")
  private UUID id = null;

  @SerializedName("code")
  private String code = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("lesson")
  private LessonItem lesson = null;

  @SerializedName("creator")
  private User creator = null;

  @SerializedName("students")
  private List<User> students = null;

  @SerializedName("lastUpdateTime")
  private DateTime lastUpdateTime = null;

  public Classroom id(UUID id) {
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

  public Classroom code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Get code
   * @return code
  **/
  @ApiModelProperty(value = "")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Classroom name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Classroom lesson(LessonItem lesson) {
    this.lesson = lesson;
    return this;
  }

   /**
   * Get lesson
   * @return lesson
  **/
  @ApiModelProperty(value = "")
  public LessonItem getLesson() {
    return lesson;
  }

  public void setLesson(LessonItem lesson) {
    this.lesson = lesson;
  }

  public Classroom creator(User creator) {
    this.creator = creator;
    return this;
  }

   /**
   * Get creator
   * @return creator
  **/
  @ApiModelProperty(value = "")
  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  public Classroom students(List<User> students) {
    this.students = students;
    return this;
  }

  public Classroom addStudentsItem(User studentsItem) {
    if (this.students == null) {
      this.students = new ArrayList<User>();
    }
    this.students.add(studentsItem);
    return this;
  }

   /**
   * Get students
   * @return students
  **/
  @ApiModelProperty(value = "")
  public List<User> getStudents() {
    return students;
  }

  public void setStudents(List<User> students) {
    this.students = students;
  }

  public Classroom lastUpdateTime(DateTime lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
    return this;
  }

   /**
   * Get lastUpdateTime
   * @return lastUpdateTime
  **/
  @ApiModelProperty(example = "2016-08-29T09:12:33.001+0000", value = "")
  public DateTime getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(DateTime lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Classroom classroom = (Classroom) o;
    return Objects.equals(this.id, classroom.id) &&
        Objects.equals(this.code, classroom.code) &&
        Objects.equals(this.name, classroom.name) &&
        Objects.equals(this.lesson, classroom.lesson) &&
        Objects.equals(this.creator, classroom.creator) &&
        Objects.equals(this.students, classroom.students) &&
        Objects.equals(this.lastUpdateTime, classroom.lastUpdateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, name, lesson, creator, students, lastUpdateTime);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Classroom {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    lesson: ").append(toIndentedString(lesson)).append("\n");
    sb.append("    creator: ").append(toIndentedString(creator)).append("\n");
    sb.append("    students: ").append(toIndentedString(students)).append("\n");
    sb.append("    lastUpdateTime: ").append(toIndentedString(lastUpdateTime)).append("\n");
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

