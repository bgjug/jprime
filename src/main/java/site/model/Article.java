package site.model;

import org.hibernate.Hibernate;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Article extends AbstractEntity{
	/**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;
	
    @NotNull
	private String title;
	
	private String description;
	
	@NotNull
    @Lob
    @Column(length=10000)
	private String text;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "author", nullable = true, referencedColumnName = "id")
    private User author;

    //Changed to eager, session problems! TODO:rethink!
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Tag.class)
    @JoinTable(name = "tags_articles", joinColumns = @JoinColumn(name = "article_pk"), inverseJoinColumns = @JoinColumn(name = "tag_pk"), indexes = {
                    @Index(columnList = "article_pk") })
    private Collection<Tag> tags = new HashSet<>();
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Collection<Tag> getTags() {
        return tags;
	}

	public void setTags(Collection<Tag> tags) {
		this.tags = tags;
	}
}

