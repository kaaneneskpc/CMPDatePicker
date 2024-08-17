package com.kaaneneskpc.whellpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import network.chaintech.utils.DateTimePickerView
import network.chaintech.utils.now
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import network.chaintech.ui.datepicker.WheelDatePickerView
import network.chaintech.ui.datetimepicker.WheelDateTimePickerComponent
import network.chaintech.ui.datetimepicker.WheelDateTimePickerView
import network.chaintech.utils.TimeFormat
import network.chaintech.utils.WheelPickerDefaults
import java.time.format.DateTimeFormatter

@Composable
fun App() {
    var showBottomSheetDatePicker by remember { mutableStateOf(false) }
    var showDialogDatePicker by remember { mutableStateOf(false) }
    var showCustomDatePicker by remember { mutableStateOf(false) }
    var selectedDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackBarHostState)

    Scaffold(
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { showBottomSheetDatePicker = true }) {
                Text("Open Bottom Sheet Picker")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { showDialogDatePicker = true }) {
                Text("Open Dialog Picker")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { showCustomDatePicker = true }) {
                Text("Open Custom Picker")
            }

            // Bottom Sheet Date Picker
            WheelDatePickerView(
                showDatePicker = showBottomSheetDatePicker,
                dateTimePickerView = DateTimePickerView.BOTTOM_SHEET_VIEW,
                onDoneClick = {
                    showBottomSheetDatePicker = false
                    selectedDate = it
                    showSnackBar(scope, snackBarHostState, it)
                },
                onDismiss = { showBottomSheetDatePicker = false }
            )

            // Dialog Date Picker
            WheelDateTimePickerView(
                showDatePicker = showDialogDatePicker,
                dateTimePickerView = DateTimePickerView.DIALOG_VIEW,onDoneClick = {
                    showDialogDatePicker = false
                    selectedDateTime = it
                    showSnackBar(scope, snackBarHostState, it)
                },
                onDismiss = { showDialogDatePicker = false }
            )

            // Custom Date Picker
            if (showCustomDatePicker) {
                WheelDateTimePickerCustom(
                    onDoneClick = {
                        showCustomDatePicker = false
                    }
                )
            }
        }
    }
}

private fun showSnackBar(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    dateTime: Any?,
) {
    val formattedDateTime = when (dateTime) {
        is LocalDateTime -> dateTime.toJavaLocalDateTime().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        )
        is LocalDate -> dateTime.toJavaLocalDate().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        )
        else -> ""
    }

    scope.launch {
        snackBarHostState.showSnackbar(
            message = "Selected date: $formattedDateTime"
        )
    }
}

@Composable
fun WheelDatePickerView(
    showDatePicker: Boolean,
    dateTimePickerView: DateTimePickerView,
    onDoneClick: (LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    WheelDatePickerView(
        height = 200.dp,
        showDatePicker = showDatePicker,
        dateTimePickerView = dateTimePickerView,
        rowCount = 3,
        showShortMonths = false,
        titleStyle = TextStyle(
            fontSize = MaterialTheme.typography.h6.fontSize,
            color = MaterialTheme.colors.onSurface.copy(0.5f)
        ),
        doneLabelStyle = TextStyle(
            fontSize = MaterialTheme.typography.body2.fontSize,
            color = MaterialTheme.colors.primary
        ),
        yearsRange = 1900..LocalDateTime.now().year,
        onDoneClick = {
            onDoneClick(it)
        },
        onDismiss = {
            onDismiss()
        }
    )
}

@Composable
fun WheelDateTimePickerView(
    showDatePicker: Boolean,
    dateTimePickerView: DateTimePickerView,
    onDoneClick: (LocalDateTime?) -> Unit,
    onDismiss: () -> Unit
) {
    WheelDateTimePickerView(
        modifier = Modifier.padding(20.dp),
        height = 200.dp,
        showDatePicker = showDatePicker,
        dateTimePickerView = dateTimePickerView,
        rowCount = 3,
        titleStyle = TextStyle(
            fontSize = MaterialTheme.typography.h6.fontSize,
            color = MaterialTheme.colors.onSurface.copy(0.5f)
        ),
        doneLabelStyle = TextStyle(
            fontSize = MaterialTheme.typography.body2.fontSize,
            color = MaterialTheme.colors.primary
        ),
        yearsRange = 1900..LocalDateTime.now().year,
        onDoneClick = {
            onDoneClick(it)
        },
        onDismiss = {
            onDismiss()
        }
    )
}


@Composable
fun WheelDateTimePickerCustom(
    onDoneClick: () -> Unit,
) {
    var date by remember { mutableStateOf("--") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(
            modifier = Modifier.fillMaxSize().background(Color.White),
            verticalArrangement = Arrangement.Center
        ) {
            Divider(
                modifier = Modifier.padding(top = 10.dp),
                thickness = (0.5).dp,
                color = Color.LightGray
            )
            WheelDateTimePickerComponent.WheelDateTimePicker(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .height(200.dp),
                titleStyle = TextStyle(
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    color = MaterialTheme.colors.onSurface.copy(0.5f)
                ),
                doneLabelStyle = TextStyle(
                    fontSize = MaterialTheme.typography.body2.fontSize,
                    color = MaterialTheme.colors.primary
                ),
                dateTextColor = Color(0xff007AFF),
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    borderColor = Color.LightGray,
                ),
                timeFormat = TimeFormat.AM_PM,
                rowCount = 5,
                height = 180.dp,
                dateTextStyle = TextStyle(
                    fontWeight = FontWeight(600),
                ),
                onDateChangeListener = { dateTime ->
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")
                    date = dateTime.toJavaLocalDateTime().format(formatter)
                },
                hideHeader = true
            )
            Divider(
                modifier = Modifier.padding(top = 10.dp),
                thickness = (0.5).dp,
                color = Color.LightGray
            )
            Row(
                modifier = Modifier.padding(top = 12.dp, start = 22.dp, end = 22.dp).fillMaxWidth()
            ) {
                Text(
                    text = "Selected Date & Time :",
                    modifier = Modifier,
                )
                Spacer(modifier = Modifier.weight(1f).height(1.dp))
                Text(
                    text = date,
                    modifier = Modifier,
                )
            }
            Divider(
                modifier = Modifier.padding(top = 12.dp),
                thickness = (0.5).dp,
                color = Color.LightGray
            )
        }
        IconButton(
            onClick = onDoneClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close"
            )
        }
    }
}



