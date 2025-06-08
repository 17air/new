package com.example.cardify.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cardify.auth.TokenManager
import com.example.cardify.models.CardCreationViewModel
import com.example.cardify.models.LoginViewModel
import com.example.cardify.models.MainScreenViewModel
import com.example.cardify.ui.screens.CreateConfirmScreen
import com.example.cardify.ui.screens.CreateDesignScreen
import com.example.cardify.ui.screens.CreateEssentialsScreen
import com.example.cardify.ui.screens.CreateProgressScreen
import com.example.cardify.ui.screens.CreateQuestionScreen
import com.example.cardify.ui.screens.LoginScreen
import com.example.cardify.ui.screens.MainEmptyScreen
import com.example.cardify.ui.screens.MainExistScreen
import com.example.cardify.ui.screens.OcrNerScreen
import com.example.cardify.ui.screens.CardBookScreen
import com.example.cardify.ui.screens.SettingsScreen
import com.example.cardify.models.CardBookViewModel
import com.example.cardify.ui.screens.RegisterCompleteScreen
import com.example.cardify.ui.screens.RegisterScreen
import com.example.cardify.ui.screens.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object RegisterComplete : Screen("register_complete_screen")
    object Main : Screen("main_screen") //
    object CreateEssentials : Screen("create_essentials")
    object CreateQuestion : Screen("create_question")
    object CreateProgress : Screen("create_progress")
    object CreateDesign : Screen("create_design")
    object CreateConfirm : Screen("create_confirm")
    object OcrNer : Screen("ocr_ner_screen")
    object CardBook : Screen("card_book_screen")
    object CardDetail : Screen("card_detail/{cardId}") {
        fun createRoute(cardId: String) = "card_detail/$cardId"
    }
    object Settings : Screen("settings_screen")
    companion object {
        val screens = listOf(
            Splash,
            Login,
            Register,
            RegisterComplete,
            Main,
            CreateEssentials,
            CreateQuestion,
            CreateProgress,
            CreateDesign,
            CreateConfirm,
            OcrNer,
            CardBook,
            CardDetail,
            Settings
        )
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val cardCreationViewModel: CardCreationViewModel = viewModel()
    val uiState by cardCreationViewModel.uiState.collectAsState()
    val cardInfo = uiState.card
    val currentQuestion by cardCreationViewModel.currentQuestion.collectAsState()
    val selectedCardId by cardCreationViewModel.selectedCardId.collectAsState()

    val loginViewModel: LoginViewModel = viewModel()
    val cardBookViewModel: CardBookViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(route = Screen.Splash.route) {
            SplashScreen(onNavigateToLogin = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) { // ✅ 수정됨: 로그인 후 Main으로 이동
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                loginViewModel = loginViewModel
            )
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(
                onNavigateToRegisterComplete = {
                    navController.navigate(Screen.RegisterComplete.route)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(route = Screen.RegisterComplete.route) {
            RegisterCompleteScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Main.route) { // ✅ 새로 추가됨: Main route에서 분기 처리
            val context = LocalContext.current
            val tokenManager = remember { TokenManager(context) }
            val token = tokenManager.getToken()
            val cardViewModel: MainScreenViewModel = viewModel()

            val cards by cardViewModel.cards.collectAsState()
            val error by cardViewModel.error.collectAsState()

            LaunchedEffect(Unit) {
                if (token != null) cardViewModel.fetchCards(token)
            }

            if (error != null) {
                Text("에러 발생: $error")
            } else {
                if (cards.isEmpty()) {
                    MainEmptyScreen(
                        onCreateCardWithAI = {
                            navController.navigate(Screen.OcrNer.route)
                        },
                        onAddExistingCard = { navController.navigate(Screen.OcrNer.route) },
                        onNavigateToCardBook = { navController.navigate(Screen.CardBook.route) },
                        onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                    )
                } else {
                    MainExistScreen(
                        cardList = cards,
                        onCardClick = { card ->
                            navController.navigate(Screen.CardDetail.createRoute(card.cardId))
                        },
                        onAddCard = { navController.navigate(Screen.OcrNer.route) },
                        onCreateNewCard = {
                            cardCreationViewModel.resetCreation()
                            navController.navigate(Screen.CreateEssentials.route)
                        },
                        onNavigateToCardBook = { navController.navigate(Screen.CardBook.route) },
                        onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                    )
                }
            }
        }

        composable(route = Screen.CreateEssentials.route) {
            val tokenManager = TokenManager(LocalContext.current)
            val token = tokenManager.getToken() ?: ""

            CreateEssentialsScreen(
                cardInfo = cardInfo,
                onCardInfoChange = { cardCreationViewModel.updateCardInfo(it) },
                onNextClick = { navController.navigate(Screen.CreateQuestion.route) },
                onBackClick = { navController.popBackStack() },
                viewModel = cardCreationViewModel,
                token = token
            )
        }

        composable(route = Screen.CreateQuestion.route) {
            CreateQuestionScreen(
                questionNumber = currentQuestion,
                onAnswerSelected = { answer ->
                    cardCreationViewModel.recordAnswer(currentQuestion, answer)
                    if (currentQuestion >= 5) {
                        navController.navigate(Screen.CreateProgress.route)
                    }
                },
                onCancelClick = {
                    cardCreationViewModel.resetCreation()
                    navController.popBackStack(Screen.CreateEssentials.route, inclusive = true)
                },
            )
        }

        composable(route = Screen.CreateProgress.route) {
            val tokenManager = TokenManager(LocalContext.current)
            val token = tokenManager.getToken() ?: ""

            CreateProgressScreen(
                cardInfo = cardInfo,
                viewModel = cardCreationViewModel,
                token = token,
                cardBookViewModel = cardBookViewModel,
                onProgressComplete = {
                    navController.navigate(Screen.CreateDesign.route)
                },
                onCancelClick = {
                    cardCreationViewModel.resetCreation()
                    navController.popBackStack(Screen.CreateEssentials.route, inclusive = true)
                }
            )
        }

        composable(route = Screen.CreateDesign.route) {
            CreateDesignScreen(
                isFirst = true,
                onCardSelected = { cardId, _ ->
                    cardCreationViewModel.selectAndSaveCard(cardId)
                    navController.navigate(Screen.CreateDesign.route + "?showOptions=true")
                },
                onCancelClick = {
                    cardCreationViewModel.resetCreation()
                    navController.popBackStack(Screen.CreateEssentials.route, inclusive = true)
                }
            )
        }

        composable(route = Screen.CreateDesign.route + "?showOptions=true") {
            CreateDesignScreen(
                isFirst = false,
                onCardSelected = { cardId, _ ->
                    cardCreationViewModel.selectAndSaveCard(cardId)
                    navController.navigate(Screen.CreateConfirm.route)
                },
                onCancelClick = {
                    cardCreationViewModel.resetCreation()
                    navController.popBackStack(Screen.CreateEssentials.route, inclusive = true)
                }
            )
        }

        composable(route = Screen.CreateConfirm.route) {
            CreateConfirmScreen(
                isComplete = false,
                selectedCardId = selectedCardId,
                onConfirmClick = {
                    navController.navigate(Screen.CreateConfirm.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.CreateConfirm.route) {
            CreateConfirmScreen(
                isComplete = true,
                selectedCardId = selectedCardId,
                onConfirmClick = {},
                onBackClick = {},
                onAddDetailsClick = {},
                onShareClick = {},
                onHomeClick = {
                    navController.navigate(Screen.Main.route) { // ✅ 수정됨: 명함 생성 후 Main으로 이동
                        popUpTo(Screen.CreateEssentials.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.OcrNer.route) {
            OcrNerScreen(viewModel = cardBookViewModel) {
                navController.navigate(Screen.CardBook.route) {
                    popUpTo(Screen.OcrNer.route) { inclusive = true }
                }
            }
        }

        composable(route = Screen.CardBook.route) {
            val cards by cardBookViewModel.cards.collectAsState()
            val highlighted by cardBookViewModel.highlightedCardId.collectAsState()
            CardBookScreen(
                cards = cards,
                highlightedCardId = highlighted,
                onNavigateToMain = { navController.navigate(Screen.Main.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onUpdateCard = { cardBookViewModel.updateCard(it) }
            )
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController) {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Main.route) { inclusive = true }
                }
            }
        }
        composable(route = Screen.CardDetail.route + "/{cardId}") {
            val cardId = it.arguments?.getString("cardId")
            // TODO: Implement CardDetailScreen
        }
    }
}
    
