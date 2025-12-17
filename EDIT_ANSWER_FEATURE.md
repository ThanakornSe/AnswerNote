# Edit Answer Feature - Navigation from Summary to Answer Sheet

## Overview
Implemented the ability for users to navigate back to the Answer Sheet screen from the Summary screen to edit their answers. This provides a seamless workflow for users who want to change their answers after reviewing the summary.

## Changes Made

### 1. **SummaryScreen.kt**

#### Added Edit Icon Import
```kotlin
import androidx.compose.material.icons.filled.Edit
```

#### Updated SummaryScreen Composable
Added `onEditAnswer` callback parameter:
```kotlin
@Composable
fun SummaryScreen(
    viewModel: AnswerSheetViewModel,
    onNavigateBack: () -> Unit,
    onEditAnswer: () -> Unit = {}  // ✅ New parameter
)
```

#### Updated SummaryQuestionItem Composable
Added:
- `onEdit` callback parameter
- Edit button in the action buttons row
- Proper parameter ordering (modifier before optional callbacks)

**Edit Button UI:**
```kotlin
Box(
    modifier = Modifier
        .size(48.dp)
        .background(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(8.dp)
        )
        .clickable(onClick = onEdit),
    contentAlignment = Alignment.Center
) {
    Icon(
        Icons.Default.Edit,
        contentDescription = "Edit Answer",
        tint = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier.size(24.dp)
    )
}
```

#### Button Layout Order
Each question item now has 3 action buttons:
1. **Edit** (Secondary Container color)
2. **Correct** (Green)
3. **Wrong** (Red)

### 2. **MainActivity.kt**

#### Updated SummaryMainScreen Entry
Added navigation callback to go back to AnswerSheetScreen:
```kotlin
SummaryScreen(
    viewModel = viewModel,
    onNavigateBack = { backStack.removeLastOrNull() },
    onEditAnswer = { backStack.removeLastOrNull() }  // ✅ Navigate back
)
```

## User Flow

### Before:
```
AnswerSheetListScreen
    ↓
AnswerSheetMainScreen → Fill answers
    ↓
SummaryMainScreen → Review & Grade
    ↓
[Stuck here, can't edit]
```

### After:
```
AnswerSheetListScreen
    ↓
AnswerSheetMainScreen ←──────┐
    ↓                        │
    Fill answers             │
    ↓                        │
SummaryMainScreen            │
    ↓                        │
    Review & Grade           │
    ↓                        │
    Click Edit button ───────┘
    (Goes back to edit)
```

## UI Design

### Question Item Layout:
```
┌─────────────────────────────────────────────────┐
│  1.    [A]    [Edit] [✓] [✗]                   │
│                                                 │
│  2.    [B]    [Edit] [✓] [✗]                   │
│                                                 │
│  3.    [C]    [Edit] [✓] [✗]                   │
└─────────────────────────────────────────────────┘
```

**Button Colors:**
- **Edit**: Secondary container (typically purple/blue tint)
- **Correct**: Green (#4CAF50)
- **Wrong**: Red (#F44336)

## Benefits

✅ **Seamless Editing**: Users can quickly go back to change answers
✅ **Non-destructive**: Grading state is preserved when going back
✅ **Intuitive**: Edit button is clearly visible and accessible
✅ **Efficient Workflow**: No need to navigate through multiple screens
✅ **Data Persistence**: Changes are auto-saved via ViewModel
✅ **Flexible**: Users can edit, re-check, and grade again

## Technical Details

### Navigation Stack Behavior
When user clicks Edit:
1. `onEditAnswer()` is called
2. `backStack.removeLastOrNull()` removes SummaryScreen
3. User returns to AnswerSheetScreen
4. ViewModel maintains the same data (same instance via Koin)
5. User can modify answers
6. User can navigate back to Summary to see updated results

### State Management
- **ViewModel**: Shared between AnswerSheetScreen and SummaryScreen
- **Data Persistence**: Auto-saved to Room database on every change
- **Reactive UI**: Score updates automatically when returning to Summary

## Usage Example

**Scenario**: User realizes they selected wrong answer

1. User is on SummaryScreen viewing graded answers
2. Notices Question 5 marked as wrong
3. Clicks **Edit button** on Question 5's row
4. Navigates back to AnswerSheetScreen
5. Scrolls to Question 5
6. Changes answer from B to C
7. Clicks "View Summary" button
8. Returns to SummaryScreen
9. Re-grades the answer
10. Sees updated score

## Files Modified

1. **SummaryScreen.kt**
   - Added Edit icon import
   - Added `onEditAnswer` parameter to SummaryScreen
   - Added `onEdit` parameter to SummaryQuestionItem
   - Added Edit button to question items
   - Reordered parameters for best practices

2. **MainActivity.kt**
   - Added `onEditAnswer` callback to SummaryScreen entry
   - Implemented back navigation logic

## Future Enhancements (Optional)

- **Scroll to Question**: Navigate to the specific question that needs editing
- **Confirmation Dialog**: Ask user to confirm before leaving graded answers
- **Highlight Changes**: Show which answers were modified after returning
- **Quick Edit Mode**: Edit inline without full navigation
- **Batch Edit**: Select multiple questions to edit at once

