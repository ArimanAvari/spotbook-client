package com.spotbook.personalguide.presentation.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.spotbook.personalguide.domain.model.User

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: suspend (User) -> Unit,
    onBackClick: () -> Unit
) {
    val state = viewModel.state

    AuthLayout(
        title = "Регистрация",
        subtitle = "Создайте аккаунт для синхронизации мест между устройствами."
    ) {
        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Повторите пароль") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = { viewModel.register(onRegisterSuccess) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            Text("Зарегистрироваться")
        }
        TextButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            Text("Уже есть аккаунт? Войти")
        }
    }
}
