package cn.cqut.lgqs.db.dao;

import cn.cqut.lgqs.db.domain.LgqsRegion;
import cn.cqut.lgqs.db.domain.LgqsRegionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LgqsRegionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    long countByExample(LgqsRegionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    int deleteByExample(LgqsRegionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    int insert(LgqsRegion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    int insertSelective(LgqsRegion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    LgqsRegion selectOneByExample(LgqsRegionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    LgqsRegion selectOneByExampleSelective(@Param("example") LgqsRegionExample example, @Param("selective") LgqsRegion.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    List<LgqsRegion> selectByExampleSelective(@Param("example") LgqsRegionExample example, @Param("selective") LgqsRegion.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    List<LgqsRegion> selectByExample(LgqsRegionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    LgqsRegion selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") LgqsRegion.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    LgqsRegion selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") LgqsRegion record, @Param("example") LgqsRegionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") LgqsRegion record, @Param("example") LgqsRegionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(LgqsRegion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_region
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(LgqsRegion record);
}