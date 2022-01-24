package model;

import java.sql.Date;

public class Document {
    private int id;
    private String docName;
    private String docText;
    private Date docDate;
    private int idAuthor;

    public Document(int id, String docName, String docText, Date docDate, int idAuthor) {
        this.id = id;
        this.docName = docName;
        this.docText = docText;
        this.docDate = docDate;
        this.idAuthor = idAuthor;
    }

    public int getId() {
        return id;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocText() {
        return docText;
    }

    public void setDocText(String docText) {
        this.docText = docText;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public int getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", docName='" + docName + '\'' +
                ", docText='" + docText + '\'' +
                ", docDate=" + docDate +
                ", idAuthor=" + idAuthor +
                '}';
    }
}
