package model;

public class Author {
    private int id;
    private String person;
    private String comments;

    public Author(int id, String person, String comments) {
        this.id = id;
        this.person = person;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
