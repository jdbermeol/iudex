package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.vo.CommentRatingVo;

@javax.persistence.Entity(name = "CommentRating")
@NamedQueries({
	@NamedQuery(name = "getCommentRatingByCommentId",
	query = "SELECT result FROM CommentRating result "
	+ "WHERE result.comment.id = :commentId"),
	@NamedQuery(name = "getCommentRatingByCommentIdAndUserId",
	query = "SELECT result FROM CommentRating result "
	+ "WHERE result.comment.id = :commentId AND result.user.id = :userId"),
        @NamedQuery(name = "getCommentRatingByUserId",
	query = "SELECT result FROM CommentRating result "
	+ "WHERE result.user.id = :userId"),
	@NamedQuery(name = "countPositiveCommentRating", query = "SELECT COUNT(result) FROM CommentRating result "
	+ "WHERE result.comment.id = :commentId AND result.value = 1"),
	@NamedQuery(name = "countNegativeCommentRating", query = "SELECT COUNT(result) FROM CommentRating result "
	+ "WHERE result.comment.id = :commentId AND result.value = -1")
})
@Table(name = "COMMENT_RATING")
public class CommentRatingEntity implements Serializable, Entity<CommentRatingVo> {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_COMMENT_RATING")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "ID_COMMENT_", nullable = false)
	private CommentEntity comment;
	@ManyToOne
	@JoinColumn(name = "ID_USER_", nullable = false)
	private UserEntity user;
	@Column(name = "RATING", nullable = false)
	private int value;

	@Override
	public CommentRatingVo toVo() {
		CommentRatingVo vo = new CommentRatingVo();
		vo.setId(getId());
		vo.setCommentId(getComment().getId());
		vo.setUserId(getUser().getId());
		vo.setValue(getValue());
		return vo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CommentRatingEntity other = (CommentRatingEntity) obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "CommentRatingEntity{" + "id=" + id + ", comment=" + comment + ", user=" + user + ", value=" + value + '}';
	}

	public CommentEntity getComment() {
		return comment;
	}

	public void setComment(CommentEntity comment) {
		this.comment = comment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
