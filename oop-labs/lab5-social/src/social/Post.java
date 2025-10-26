package social;

import java.lang.invoke.StringConcatException;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Post  {
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="author_code",insertable=false, updatable=false)
    private Person author;


    @Id
    String id;
    String text;
    String author_code;
    long timestamp;
    public Post(){}
    
    public Post (String id, String text, String author_code, long timestamp , Person author){
        this.id=id;
        this.text=text;
        this.author_code=author_code;
        this.timestamp=timestamp;
        this.author=author;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getAuthor_code() {
        return author_code;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
