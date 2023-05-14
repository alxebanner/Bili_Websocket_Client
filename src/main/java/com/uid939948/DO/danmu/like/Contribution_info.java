/**
  * Copyright 2023 bejson.com 
  */
package com.uid939948.DO.danmu.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Auto-generated: 2023-04-01 17:38:54
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contribution_info {

    private int grade;
    public void setGrade(int grade) {
         this.grade = grade;
     }
     public int getGrade() {
         return grade;
     }

}