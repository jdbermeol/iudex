package org.xtremeware.iudex.dao.sql;

import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.CommentRatingEntity;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.entity.SubjectRatingEntity;

/**
 * DAO factory for a MySQL persistence unit
 *
 * @author healarconr
 */
public class MySqlDaoFactory implements AbstractDaoFactory {

    private CommentDao commentDao;
    private BinaryRatingDao<CommentRatingEntity> commentRatingDao;
    private ConfirmationKeyDao confirmationKeyDao;
    private CourseDao courseDao;
    private CourseRatingDao courseRatingDao;
    private FeedbackDao feedbackDao;
    private FeedbackTypeDao feedbackTypeDao;
    private PeriodDao periodDao;
    private ProfessorDao professorDao;
    private BinaryRatingDao<ProfessorRatingEntity> professorRatingDao;
    private ProgramDao programDao;
    private SubjectDao subjectDao;
    private BinaryRatingDao<SubjectRatingEntity> subjectRatingDao;
    private UserDao userDao;
    private static MySqlDaoFactory instance;

    private MySqlDaoFactory() {
    }

    public static synchronized MySqlDaoFactory getInstance() {
        if (instance == null) {
            instance = new MySqlDaoFactory();
        }
        return instance;
    }

    @Override
    public CommentDao getCommentDao() {
        if (commentDao == null) {
            commentDao = new SQLCommentDao();
        }
        return commentDao;
    }

    @Override
    public BinaryRatingDao<CommentRatingEntity> getCommentRatingDao() {
        if (commentRatingDao == null) {
            commentRatingDao = new SQLCommentRatingDao();
        }
        return commentRatingDao;
    }

    @Override
    public ConfirmationKeyDao getConfirmationKeyDao() {
        if (confirmationKeyDao == null) {
            confirmationKeyDao = new SQLConfirmationKeyDao();
        }
        return confirmationKeyDao;
    }

    @Override
    public CourseDao getCourseDao() {
        if (courseDao == null) {
            courseDao = new SQLCourseDao();
        }
        return courseDao;
    }

    @Override
    public CourseRatingDao getCourseRatingDao() {
        if (courseRatingDao == null) {
            courseRatingDao = new SQLCourseRatingDao();
        }
        return courseRatingDao;
    }

    @Override
    public FeedbackDao getFeedbackDao() {
        if (feedbackDao == null) {
            feedbackDao = new SQLFeedbackDao();
        }
        return feedbackDao;
    }

    @Override
    public FeedbackTypeDao getFeedbackTypeDao() {
        if (feedbackTypeDao == null) {
            feedbackTypeDao = new SQLFeedbackTypeDao();
        }
        return feedbackTypeDao;
    }

    @Override
    public PeriodDao getPeriodDao() {
        if (periodDao == null) {
            periodDao = new SQLPeriodDao();
        }
        return periodDao;
    }

    @Override
    public ProfessorDao getProfessorDao() {
        if (professorDao == null) {
            professorDao = new SQLProfessorDao();
        }
        return professorDao;
    }

    @Override
    public BinaryRatingDao<ProfessorRatingEntity> getProfessorRatingDao() {
        if (professorRatingDao == null) {
            professorRatingDao = new SQLProfessorRatingDao();
        }
        return professorRatingDao;
    }

    @Override
    public ProgramDao getProgramDao() {
        if (programDao == null) {
            programDao = new SQLProgramDao();
        }
        return programDao;
    }

    @Override
    public SubjectDao getSubjectDao() {
        if (subjectDao == null) {
            subjectDao = new SQLSubjectDao();
        }
        return subjectDao;
    }

    @Override
    public BinaryRatingDao<SubjectRatingEntity> getSubjectRatingDao() {
        if (subjectRatingDao == null) {
            subjectRatingDao = new SQLSubjectRatingDao();
        }
        return subjectRatingDao;
    }

    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new SQLUserDao();
        }
        return userDao;
    }
}
