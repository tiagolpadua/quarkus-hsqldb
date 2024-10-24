package org.acme.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.acme.models.Book;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

// https://medium.com/@tachi.hatim/integrating-apache-lucene-with-quarkus-for-full-text-search-337c2647f56e
// https://www.baeldung.com/lucene

@ApplicationScoped
@Slf4j
public class LuceneService {
    Directory directory;
    StandardAnalyzer analyzer;
    IndexWriter indexWriter;

    @PostConstruct
    public void init() throws IOException {
        log.info("Initializing LuceneService");
        analyzer = new StandardAnalyzer();
        directory = FSDirectory.open(Paths.get("lucene-data"));
    }

    public void openWriter() throws IOException {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(directory, indexWriterConfig);
    }

    public void closeWriter() throws IOException {
        indexWriter.close();
    }

    public void indexBook(Book book) throws IOException {
        Document document = new Document();
        document.add(new TextField("title", book.getTitle(), Field.Store.YES));
        document.add(new TextField("author", book.getAuthor(), Field.Store.YES));
        indexWriter.addDocument(document);
    }

    public List<Document> searchBook(String inField, String queryString) throws ParseException, IOException {
        Query query = new QueryParser(inField, analyzer).parse(queryString);

        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(indexReader);
        TopDocs topDocs = searcher.search(query, 10);
        List<Document> documents = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            documents.add(searcher.doc(scoreDoc.doc));
        }

        return documents;
    }

}
