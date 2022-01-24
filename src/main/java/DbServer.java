import com.mysql.cj.jdbc.Driver;
import model.Author;
import model.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbServer implements IDbService {
    private static DbServer dbServer;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private DbServer() {
    }

    private void getDBConnection() throws DocumentException {
        if (connection == null) {
            Settings settings = Settings.getSettings();
            try {
                Driver driver = new Driver();
                DriverManager.registerDriver(driver);
                connection = DriverManager.getConnection(
                        settings.getConnectionData()[0],
                        settings.getConnectionData()[1],
                        settings.getConnectionData()[2]
                );
                System.out.println("Соединение с БД установлено");
            } catch (SQLException e) {
                throw new DocumentException("Не удалось подключиться к БД");
            }
        }
    }

    public static DbServer getDbServer() {
        if (dbServer == null) dbServer = new DbServer();
        return dbServer;
    }

    @Override
    public boolean addAuthor(Author author) throws DocumentException {
        getDBConnection();
        if (author == null) throw new DocumentException("передоваемый параметр null");
        try {
            if (author.getId() == 0 && author.getPerson() != null && !author.getPerson().trim().equals("")) {
                if (statement == null) statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO authors (authors.person, authors.comments) VALUES ('" +
                        author.getPerson() + "', " + author.getComments() + ");");
                return true;
            } else if (author.getId() == 0) {
                throw new DocumentException("Обязательное поле ФИО автора - пустое или null");
            } else if (author.getId() != 0 && author.getPerson() != null && !author.getPerson().trim().equals("")) {
                if (statement == null) statement = connection.createStatement();
                statement.executeUpdate("UPDATE authors SET comments='" + author.getComments() +
                        "' WHERE authors.id=" + author.getId() + ";");
                return false;
            } else throw new DocumentException("Обновляемое обязательное поле - null или пустое");
        } catch (SQLException e) {
            throw new DocumentException("Не удалось подключиться к БД");
        }
    }

    @Override
    public boolean addDocument(Document doc, Author author) throws DocumentException {
        getDBConnection();
        if (author == null || doc == null) throw new DocumentException("передоваемый параметр null");
        try {
            if (doc.getId() == 0 &&
                    author.getId() != 0 &&
                    author.getPerson() != null &&
                    !author.getPerson().trim().equals("") &&
                    doc.getIdAuthor() != 0 &&
                    doc.getDocName() != null &&
                    !doc.getDocName().trim().equals("") &&
                    doc.getDocDate() != null) {
                if (statement == null) statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO documents (documents.docName, documents.docText, documents.docDate, documents.idAuthor) " +
                        "VALUES ('" + doc.getDocName() + "', '" + doc.getDocText() + "', '" + doc.getDocDate() + "', " + doc.getIdAuthor() + ");");
                return true;
            } else if (doc.getId() == 0) {
                throw new DocumentException("Обязательное поле документа или автора - пустое или null");
            } else if (doc.getId() != 0 &&
                    doc.getIdAuthor() != 0 &&
                    doc.getDocName() != null &&
                    !doc.getDocName().trim().equals("") &&
                    doc.getDocDate() != null) {
                if (statement == null) statement = connection.createStatement();
                statement.executeUpdate("UPDATE documents SET " +
                        "documents.docName='" + doc.getDocName() + "', documents.docText='" + doc.getDocText() + "', " +
                        "documents.docDate='" + doc.getDocDate() + "', " +
                        "documents.idAuthor=" + doc.getIdAuthor() + " WHERE documents.id=" + doc.getId() + ";");
                return false;
            } else throw new DocumentException("Обновляемое обязательное поле - null или пустое");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DocumentException("Не удалось подключиться к БД");
        }
    }

    @Override
    public Document[] findDocumentByAuthor(Author author) throws DocumentException {
        getDBConnection();
        if (author == null) throw new DocumentException("передоваемый параметр null");
        try {
            if (author.getId() != 0) {
                if (statement == null) statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM documents WHERE idAuthor=" + author.getId() + ";");
                List<Document> docs = new ArrayList<Document>();
                while (resultSet.next()) {
                    Document newDocument = new Document(resultSet.getInt(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getDate(4), resultSet.getInt(5));
                    docs.add(newDocument);
                }
                if (docs.size() == 0) return null;
                Document[] returnArray = new Document[docs.size()];
                int i = 0;
                for (Document d : docs)
                    returnArray[i++] = d;
                return returnArray;
            } else if (author.getPerson() != null && !author.getPerson().trim().equals("")) {
                if (statement == null) statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT documents.id, documents.docName, documents.docText, " +
                        "documents.docDate, documents.idAuthor FROM documents LEFT JOIN authors ON documents.idAuthor=authors.id " +
                        "WHERE authors.person='" + author.getPerson() + "';");
                List<Document> docs = new ArrayList<Document>();
                while (resultSet.next()) {
                    Document newDocument = new Document(resultSet.getInt(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getDate(4), resultSet.getInt(5));
                    docs.add(newDocument);
                }
                if (docs.size() == 0) return null;
                Document[] returnArray = new Document[docs.size()];
                int i = 0;
                for (Document d : docs)
                    returnArray[i++] = d;
                return returnArray;
            } else throw new DocumentException("Нельзя выполнить поиск по полям автора");
        } catch (SQLException e) {
            throw new DocumentException("Не удалось подключиться к БД");
        }
    }

    @Override
    public Document[] findDocumentByContent(String content) throws DocumentException {
        getDBConnection();
        if (content == null && content.trim().equals(""))
            throw new DocumentException("передоваемый параметр null или пустой");
        try {
            if (statement == null) statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM documents WHERE docText LIKE '%" + content + "%' OR docName LIKE '%" + content + "%';");
            List<Document> docs = new ArrayList<Document>();
            while (resultSet.next()) {
                Document newDocument = new Document(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getDate(4), resultSet.getInt(5));
                docs.add(newDocument);
            }
            if (docs.size() == 0) return null;
            Document[] returnArray = new Document[docs.size()];
            int i = 0;
            for (Document d : docs)
                returnArray[i++] = d;
            return returnArray;
        } catch (SQLException e) {
            throw new DocumentException("Не удалось подключиться к БД");
        }
    }

    @Override
    public boolean deleteAuthor(Author author) throws DocumentException {
        getDBConnection();
        if (author == null) throw new DocumentException("передоваемый параметр null");
        try {
            if (author.getId() != 0) {
                if (statement == null) statement = connection.createStatement();
                statement.executeUpdate("DELETE FROM documents WHERE idAuthor=" + author.getId() + ";");
                statement.executeUpdate("DELETE FROM authors WHERE id=" + author.getId() + ";");
                return true;
            } else if (author.getPerson() != null && !author.getPerson().trim().equals("")) {
                if (statement == null) statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT id FROM authors WHERE person='" + author.getPerson() + "';");
                if (!resultSet.next()) return false;
                int authorId = resultSet.getInt(1);
                statement.executeUpdate("DELETE FROM documents WHERE idAuthor=" + authorId + ";");
                statement.executeUpdate("DELETE FROM authors WHERE id=" + authorId + ";");
                return true;
            } else return false;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DocumentException("Не удалось подключиться к БД");
        }
    }

    @Override
    public boolean deleteAuthor(int id) throws DocumentException {
        getDBConnection();
        if (id == 0) return false;
        try {
            if (statement == null) statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM documents WHERE idAuthor=" + id + ";");
            statement.executeUpdate("DELETE FROM authors WHERE id=" + id + ";");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DocumentException("Не удалось подключиться к БД");
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null && connection.isValid(0)) connection.close();
        if (statement != null) statement.close();
        if (resultSet != null) resultSet.close();
        System.out.println("Соединение с БД закрыто");
    }
}
