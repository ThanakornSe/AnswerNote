# Navigation and Score Display Enhancement

## Overview
Added two key features:
1. **"Back to Answer Sheets" button** in SummaryScreen when all answers are graded
2. **Score display** in AnswerSheetListScreen for graded answer sheets

## Changes Made

### 1. SummaryScreen - Navigation Button

#### Added Parameters
```kotlin
@Composable
fun SummaryScreen(
    viewModel: AnswerSheetViewModel,
    onNavigateBack: () -> Unit,
    onEditAnswer: () -> Unit = {},
    onNavigateToList: () -> Unit = {}  // ✅ NEW
)
```

#### Logic to Check Grading Completion
```kotlin
// Check if all answered questions are graded
val answeredQuestions = answers.filter { it.selectedAnswer != Answer.NONE }
val allAnswersGraded = answeredQuestions.isNotEmpty() && 
                       answeredQuestions.all { it.correctAnswer != null }
```

#### Button Implementation
```kotlin
// Show "Back to List" button when all answers are graded
if (allAnswersGraded) {
    item {
        Button(
            onClick = onNavigateToList,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Back to Answer Sheets",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
```

**Behavior:**
- Only appears when **all answered questions have correct answers set**
- Full-width button at the bottom of the list
- Primary color to indicate primary action
- Clears the navigation stack back to list

### 2. MainActivity - Navigation Handler

#### Implementation
```kotlin
SummaryScreen(
    viewModel = viewModel,
    onNavigateBack = { backStack.removeLastOrNull() },
    onEditAnswer = { ... },
    onNavigateToList = {
        // Navigate back to list, clearing the stack
        while (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }
)
```

**Behavior:**
- Removes all screens from back stack except the AnswerSheetListScreen
- Ensures clean navigation with no lingering screens
- User can't accidentally navigate back to summary after leaving

### 3. AnswerSheetListScreen - Score Display

#### Enhanced AnswerSheetCard
Added score calculation and display:

```kotlin
// Calculate score if answers are graded
val gradedAnswers = answerSheet.answers.filter { it.correctAnswer != null }
val correctAnswers = gradedAnswers.count { it.isCorrect == true }
val isGraded = gradedAnswers.isNotEmpty()
val scorePercentage = if (gradedAnswers.isNotEmpty()) {
    (correctAnswers.toFloat() / gradedAnswers.size * 100).toInt()
} else {
    0
}
```

#### Score Display UI
```kotlin
// Show score if graded
if (isGraded) {
    Text(
        text = "Score: $correctAnswers/${gradedAnswers.size} ($scorePercentage%)",
        style = MaterialTheme.typography.bodyMedium,
        color = when {
            scorePercentage >= 80 -> Color(0xFF4CAF50) // Green for high score
            scorePercentage >= 60 -> Color(0xFFFF9800) // Orange for medium score
            else -> Color(0xFFF44336) // Red for low score
        },
        fontWeight = FontWeight.Bold
    )
}
```

**Color Coding:**
- 🟢 **80%+**: Green (#4CAF50) - Excellent
- 🟠 **60-79%**: Orange (#FF9800) - Good
- 🔴 **Below 60%**: Red (#F44336) - Needs Improvement

## User Flow

### Grading Complete Flow:
```
1. User finishes grading all answers in SummaryScreen
   ↓
2. "Back to Answer Sheets" button appears at bottom
   ↓
3. User clicks button
   ↓
4. Navigates to AnswerSheetListScreen
   ↓
5. Sees score displayed on the card
```

### What User Sees on List:

**Before Grading:**
```
┌─────────────────────────────────┐
│ TOEIC Test 1    [Completed]     │
│ Questions: 200                  │
│ Answered: 200/200               │
│ Updated: Dec 17, 2025 14:30     │
└─────────────────────────────────┘
```

**After Grading:**
```
┌─────────────────────────────────┐
│ TOEIC Test 1    [Completed]     │
│ Questions: 200                  │
│ Answered: 200/200               │
│ Score: 180/200 (90%)  🟢        │
│ Updated: Dec 17, 2025 14:30     │
└─────────────────────────────────┘
```

## Benefits

### For Summary Screen:
✅ **Clear Exit Path**: Users know when they're done grading
✅ **Convenient Navigation**: No need to use back button multiple times
✅ **Task Completion**: Visual confirmation that grading is complete
✅ **Clean Stack**: No navigation history clutter

### For List Screen:
✅ **Quick Overview**: See scores at a glance
✅ **Color Coded**: Instant visual feedback on performance
✅ **Detailed Info**: Shows both score and percentage
✅ **Historical Data**: Track progress across multiple tests
✅ **Motivation**: Green scores provide positive reinforcement

## Technical Details

### Grading Completion Logic
- Checks if `answeredQuestions.isNotEmpty()` to avoid false positive on empty tests
- Checks if `all { it.correctAnswer != null }` to ensure every answered question has been graded
- Button only appears when both conditions are true

### Score Calculation
- **Graded Answers**: Count of answers with `correctAnswer != null`
- **Correct Answers**: Count of graded answers where `isCorrect == true`
- **Percentage**: `(correctAnswers / gradedAnswers) × 100`
- **Partial Grading**: Shows score even if not all questions are graded

### Navigation Stack Cleanup
```kotlin
while (backStack.size > 1) {
    backStack.removeLastOrNull()
}
```
This ensures only AnswerSheetListScreen remains in the stack.

## Edge Cases Handled

1. **Empty Test**: Button doesn't appear if no answers
2. **Partially Graded**: Button doesn't appear until all answered questions are graded
3. **No Score Display**: Score only shows if at least one answer is graded
4. **Zero Score**: Displays "0/20 (0%)" in red if all answers are wrong
5. **Perfect Score**: Displays "20/20 (100%)" in green

## Visual Examples

### Low Score (45%):
```
Score: 9/20 (45%) 🔴
```

### Medium Score (75%):
```
Score: 15/20 (75%) 🟠
```

### High Score (95%):
```
Score: 19/20 (95%) 🟢
```

## Files Modified

1. **SummaryScreen.kt**
   - Added `onNavigateToList` parameter
   - Added `allAnswersGraded` state calculation
   - Added "Back to Answer Sheets" button
   - Added Button import

2. **MainActivity.kt**
   - Added `onNavigateToList` callback
   - Implemented stack clearing navigation

3. **AnswerSheetListScreen.kt**
   - Added Color import
   - Enhanced AnswerSheetCard with score calculation
   - Added score display with color coding

## Future Enhancements (Optional)

- **Average Score**: Show overall average across all tests
- **Best/Worst**: Highlight best and worst performing tests
- **Score History**: Chart showing score progression over time
- **Grade Letters**: Show A, B, C, D, F based on percentage
- **Achievements**: Badges for high scores or improvement
- **Export Scores**: Export all scores to CSV or PDF

