# TOEIC Answer Sheet App

A powerful Android app to help you fill out answer sheets for TOEIC exams with 200 questions, featuring dynamic answer pattern generation.

## Features

### Core Features
- **200 Questions**: Complete answer sheet with questions numbered 1-200
- **Multiple Choice Selection**: Each question has 4 options (A, B, C, D)
- **Progress Tracking**: See how many questions you've answered in the top bar
- **Summary View**: Tap the "Summary" button to see all your answers at once
- **Share Functionality**: Export and share your answers via text
- **Clear All**: Reset all answers with a single tap
- **Material Design 3**: Modern, clean UI with Material You design

### 🎯 Dynamic Pattern Generation (NEW!)
Generate answers automatically using various patterns:

#### Pattern Types:
1. **All A/B/C/D** - Fill all questions with a single answer
2. **ABCD Repeating** - Cycle through A, B, C, D repeatedly
3. **DCBA Repeating** - Reverse cycle through D, C, B, A
4. **Random** - Randomly generate answers
5. **Diagonal A→D** - Gradually transition from A to D
6. **Diagonal D→A** - Gradually transition from D to A
7. **Alternating A-B** - Alternate between A and B
8. **Alternating C-D** - Alternate between C and D

#### Apply to Sections:
- **All Questions (1-200)** - Apply pattern to entire sheet
- **Part 1-2 (1-50)** - Listening comprehension sections
- **Part 3-4 (51-100)** - Listening comprehension sections
- **Part 5-6 (101-150)** - Reading comprehension sections
- **Part 7 (151-200)** - Reading comprehension sections

## How to Use

1. **Sync Gradle**: After opening the project, Android Studio will prompt you to sync Gradle. Click "Sync Now" to download all required dependencies.

2. **Build and Run**: Once Gradle sync is complete, click the Run button to install the app on your device or emulator.

3. **Select Answers Manually**: 
   - Scroll through the list of 200 questions
   - Tap the radio button (A, B, C, or D) to select your answer for each question
   - The progress counter at the top shows how many questions you've answered

4. **Generate Patterns (NEW!)**:
   - Tap the **Settings icon** (⚙️) in the top right
   - Select a pattern from the list (e.g., "ABCD Repeating", "Random", etc.)
   - Choose which section to apply it to (all questions or specific parts)
   - Tap "Apply" to fill answers automatically

5. **View Summary**:
   - Tap the "Summary" floating action button at the bottom right
   - See all your answers organized by TOEIC sections
   - Tap "Share" to export your answers via text message, email, or other apps

6. **Clear Answers**:
   - Tap the trash icon in the top right to clear all answers
   - Confirm your choice in the dialog

## Use Cases

### For Test Practice:
- Quickly fill in practice answers to see patterns
- Test different answering strategies
- Generate random answer sheets for practice grading

### For Testing Strategies:
- Experiment with diagonal patterns (gradually changing answers)
- Try alternating patterns to see score distributions
- Compare results from different pattern types

### For Quick Setup:
- Fill sections you know instantly
- Apply patterns to unsure sections
- Mix manual selection with pattern generation

## Technical Details

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM pattern with ViewModel
- **State Management**: StateFlow for reactive UI updates
- **Minimum SDK**: Android 11 (API 30)
- **Target SDK**: Android 15 (API 36)

## Project Structure

```
app/src/main/java/com/thans/answernote/
├── MainActivity.kt                    # Main entry point
├── model/
│   └── AnswerSheet.kt                # Data models (Answer, AnswerPattern, SectionRange)
├── viewmodel/
│   └── AnswerSheetViewModel.kt       # Business logic, state management, pattern generation
└── ui/
    ├── components/
    │   ├── QuestionItem.kt           # Individual question card with radio buttons
    │   ├── SummaryDialog.kt          # Summary and share dialog
    │   └── PatternGeneratorDialog.kt # Pattern selection and application dialog (NEW!)
    ├── screen/
    │   └── AnswerSheetScreen.kt      # Main screen with question list
    └── theme/
        ├── Color.kt                  # App colors
        ├── Theme.kt                  # Material theme configuration
        └── Type.kt                   # Typography settings
```

## Dependencies

All dependencies are managed via Gradle version catalogs:
- AndroidX Core KTX
- AndroidX Lifecycle (Runtime & ViewModel Compose)
- AndroidX Activity Compose
- Jetpack Compose BOM
- Material 3

## Pattern Generation Logic

The app includes sophisticated pattern generation algorithms:
- **Repeating patterns**: Use modulo arithmetic for cyclic patterns
- **Diagonal patterns**: Calculate gradual transitions based on position
- **Random patterns**: Use Kotlin's Random for unpredictable sequences
- **Section-specific**: Apply patterns only to selected question ranges

## Notes

- Answers are stored in memory only and will be lost when the app is closed
- The app is designed for simplicity and ease of use during exam practice
- Export feature uses Android's share functionality, allowing you to send answers to any app on your device
- Pattern generation can be combined with manual selection - apply patterns first, then modify individual questions
- Exported answer sheets are organized by TOEIC sections for easy reading

