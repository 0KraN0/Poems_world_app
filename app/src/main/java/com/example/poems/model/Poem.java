package com.example.poems.model;

/**
 * Класс модели данных, описывающий структуру стихотворения.
 */
public class Poem {
    private int id;
    private String title;
    private String author;
    private String category;
    private String text;
    private boolean isFavorite;
    private String note;

    public Poem(int id, String title, String author, String category, String text, boolean isFavorite, String note) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.text = text;
        this.isFavorite = isFavorite;
        this.note = note;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getText() { return text; }
    public boolean isFavorite() { return isFavorite; }
    public String getNote() { return note; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public void setNote(String note) { this.note = note; }
}