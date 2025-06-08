package com.example.cardify.features

data class Question(
    val id: Int,
    val question: String,
    val options: List<String>
)

object QuestionBank {
    val questions = listOf(
        Question(
            id = 1,
            question = "명함에서 어떤 첫인상을 주고 싶나요?",
            options = listOf(
                "전문적이고 신뢰감 있는 이미지",
                "창의적이고 독창적인 이미지",
                "부드럽고 따뜻한 이미지",
                "강렬하고 인상적인 이미지"
            )
        ),
        Question(
            id = 2,
            question = "당신이 선호하는 스타일은 무엇인가요?",
            options = listOf(
                "모던하고 미니멀한 스타일",
                "클래식하고 정갈한 스타일",
                "자유롭고 예술적인 스타일",
                "독특하고 실험적인 스타일"
            )
        ),
        Question(
            id = 3,
            question = "선호하는 색상 계열은 무엇인가요?",
            options = listOf(
                "블랙&화이트",
                "블루&네이비",
                "파스텔톤",
                "레드&오렌지"
            )
        ),
        Question(
            id = 4,
            question = "명함에서 강조하고 싶은 정보는 무엇인가요?",
            options = listOf(
                "이름",
                "직업/직함",
                "연락처",
                "회사/브랜드명명"
            )
        ),
        Question(
            id = 5,
            question = "당신의 직업을 분위기로 표현한다면?",
            options = listOf(
                "분석적이고 정제된 직업",
                "따뜻하고 감성적인 직업",
                "도전적이고 열정적인 직업",
                "창의적이고 유연한 직업업"
            )
        ),
        Question(
            id = 6,
            question = "당신이 선호하는 명함의 분위기는 무엇인가요?",
            options = listOf(
                "세련된",
                "신뢰감 있는",
                "부드러운",
                "활기찬찬"
            )
        ),
        Question(
            id = 7,
            question = "당신이 선호하는 명함의 배경은 어떤 스타일인가요?",
            options = listOf(
                "흐릿한 사진이 적용된 스타일",
                "라인 드로잉으로 구성된 스타일",
                "벡터를 활용한 아트 스타일",
                "배경이 없는 심플한 스타일"
            )
        ),
        Question(
            id = 8,
            question = "당신이 선호하는 명함의 글씨체는 무엇인가요?",
            options = listOf(
                "고전적인 글씨체",
                "현대적이고 모던한 글씨체",
                "손글씨 느낌의 글씨체",
                "디지털 느낌의 각진 글씨체체"
            )
        ),
        Question(
            id = 9,
            question = "당신이 선호하는 명함의 항목 구성은 어떤가요?",
            options = listOf(
                "심플한 구성 - 왼쪽으로 정렬",
                "심플한 구성 - 중앙으로 정렬",
                "다채로운 구성 - 정보 분산형",
                "다채로운 구성 - 대담한 크기 배치치"
            )
        ),
        Question(
            id = 10,
            question = "당신이 선호하는 전체적인 명함의 인상은 어떤가요?",
            options = listOf(
                "전문적인 인상",
                "편안한 인상",
                "개성적인 인상",
                "에너지가 넘치는 인상"
            )
        )
    )
}
