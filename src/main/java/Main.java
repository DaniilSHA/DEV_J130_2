import model.Author;
import model.Document;

import java.io.IOException;
import java.sql.Date;

public class Main {
    public static void main(String[] args) {

        Author authorFail1 = new Author(0, null, null);
        Author authorFail2 = new Author(4, null, null);
        Author author1 = new Author(4, null, "well done");
        Author author2 = new Author(4, "Arnold Pupkin", null);


        Document documentFail1 = new Document(0, null, "text", new Date(122,10,15), 4);
        Document document1 = new Document(0, "text", "text", new Date(122,10,15), 4);
        Document documentFail2 = new Document(8, null, "text", new Date(122,10,15), 4);
        Document document2 = new Document(8, "sweg", "text", new Date(122,10,15), 4);


        try (DbServer dbServer = DbServer.getDbServer()) {
//            System.out.println(dbServer.addAuthor(authorFail1));
//            System.out.println(dbServer.addAuthor(authorFail2));
//            System.out.println(dbServer.addAuthor(author1));
//            System.out.println(dbServer.addAuthor(author2));

//            System.out.println(dbServer.addDocument(documentFail1, author2));
//            System.out.println(dbServer.addDocument(document1, author2));
//            System.out.println(dbServer.addDocument(documentFail2,author2));
//            System.out.println(dbServer.addDocument(document2, author2));

//            Document [] findDocByAuthorId = dbServer.findDocumentByAuthor(new Author(10,null,null));
//            if (findDocByAuthorId != null){
//                for (Document d : findDocByAuthorId)
//                    System.out.println(d);
//            }
//
//            Document [] findDocByAuthorName = dbServer.findDocumentByAuthor(new Author(0,"Tom Clancy",null));
//            if (findDocByAuthorName != null) {
//                for (Document d : findDocByAuthorName)
//                    System.out.println(d);
//            }

//            Document [] findDocByContent = dbServer.findDocumentByContent("content");
//            if (findDocByContent != null) {
//                for (Document d : findDocByContent)
//                    System.out.println(d);
//            }

//            System.out.println(dbServer.deleteAuthor(new Author(3,null,null)));

//            System.out.println(dbServer.deleteAuthor(2));

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
