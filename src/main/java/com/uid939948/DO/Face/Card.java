package com.uid939948.DO.Face;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
        private String mid;
        private String name;
        private boolean approve;
        private String sex;
        private String rank;
        private String face;
        private int face_nft;
        private int face_nft_type;
        private String DisplayRank;
        private int regtime;
        private int spacesta;
        private String birthday;
        private String place;
        private String description;
        private int article;
//        private List<String> attentions;
        private int fans;
        private int friend;
        private int attention;
        private String sign;
        private int is_senior_member;

}
