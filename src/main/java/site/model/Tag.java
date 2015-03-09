package site.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Tag extends AbstractEntity {
	/**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;
	
	private String name;

	@ManyToMany(fetch = FetchType.LAZY, targetEntity = Article.class)
	@JoinTable(name = "tags_articles", joinColumns = @JoinColumn(name = "tag_pk"), inverseJoinColumns = @JoinColumn(name = "article_pk"), indexes = { @Index(columnList = "tag_pk") })
	private Collection<Article> articles = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Article> getArticles() {
		return articles;
	}

	public void setArticles(Collection<Article> articles) {
		this.articles = articles;
	}

}
