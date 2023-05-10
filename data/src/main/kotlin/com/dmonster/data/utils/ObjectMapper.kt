package com.dmonster.data.utils

import com.dmonster.data.remote.dto.MemberInfoDto
import com.dmonster.data.remote.dto.TokenDto
import com.dmonster.domain.model.MemberInfoModel
import com.dmonster.domain.model.TokenModel

object ObjectMapper {
    fun TokenDto.toModel(): TokenModel = TokenModel(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
    )

    fun MemberInfoDto.toModel(): MemberInfoModel = MemberInfoModel(
        idx = this.idx,
        mt_id = this.mt_id,
        mt_name = this.mt_name,
        mt_wdate = this.mt_wdate,
        mt_type = this.mt_type,
    )
}