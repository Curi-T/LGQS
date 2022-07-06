package cn.cqut.lgqs.db.dao;

import cn.cqut.lgqs.db.domain.LgqsSearchHistory;
import cn.cqut.lgqs.db.domain.LgqsSearchHistoryExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LgqsSearchHistoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    long countByExample(LgqsSearchHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    int deleteByExample(LgqsSearchHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    int insert(LgqsSearchHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    int insertSelective(LgqsSearchHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    LgqsSearchHistory selectOneByExample(LgqsSearchHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    LgqsSearchHistory selectOneByExampleSelective(@Param("example") LgqsSearchHistoryExample example, @Param("selective") LgqsSearchHistory.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    List<LgqsSearchHistory> selectByExampleSelective(@Param("example") LgqsSearchHistoryExample example, @Param("selective") LgqsSearchHistory.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    List<LgqsSearchHistory> selectByExample(LgqsSearchHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    LgqsSearchHistory selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") LgqsSearchHistory.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    LgqsSearchHistory selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    LgqsSearchHistory selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") LgqsSearchHistory record, @Param("example") LgqsSearchHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") LgqsSearchHistory record, @Param("example") LgqsSearchHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(LgqsSearchHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(LgqsSearchHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") LgqsSearchHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_search_history
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}