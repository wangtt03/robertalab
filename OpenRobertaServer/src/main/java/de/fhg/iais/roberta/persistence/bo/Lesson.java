package de.fhg.iais.roberta.persistence.bo;

import javax.persistence.*;

enum LessonLevel {
    Beginner,
    Medium,
    Hard
}

/**
 * Created by tiantianwang on 2017/6/30.
 */
@Entity
@Table(name = "LESSON")
public class Lesson implements WithSurrogateId{
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Column(name = "DOCURL")
    private String docUrl;

    @Column(name = "PRGURL")
    private String programUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "LEVEL")
    private LessonLevel level;

    protected Lesson(){

    }

    public Lesson(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getProgramUrl() {
        return programUrl;
    }

    public void setProgramUrl(String programUrl) {
        this.programUrl = programUrl;
    }

    public LessonLevel getLevel() {
        return level;
    }

    public void setLevel(LessonLevel level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", docUrl='" + docUrl + '\'' +
                ", programUrl='" + programUrl + '\'' +
                ", level=" + level +
                '}';
    }
}
