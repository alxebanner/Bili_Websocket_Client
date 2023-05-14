package com.uid939948.DO.Emoji;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmojiInfo {
    private String emoji;

    private String emoticon_unique;

    private String url;

    private int emoticon_id;
}
