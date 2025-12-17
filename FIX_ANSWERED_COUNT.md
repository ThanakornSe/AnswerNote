# Fix: Real-time Answer Count Update

## Problem
The `answeredCount` variable was not updating in real-time when:
1. User selected/changed answers
2. User cleared all answers
3. Answer state changed in any way

## Root Cause
The original code used `viewModel.getAnsweredCount()` which was called only once during composition:
```kotlin
val answeredCount = viewModel.getAnsweredCount()
```

This meant the count was calculated only when the composable was first created and never updated afterwards, even though the `answers` state was changing.

## Solution
Changed to use `remember` with the `answers` as a key, so it recalculates whenever the answers list changes:

```kotlin
val answeredCount = remember(answers) { 
    answers.count { it.selectedAnswer != com.thans.answernote.presenter.model.Answer.NONE }
}
```

### How it works:
1. **`remember(answers)`**: Creates a remembered value that depends on the `answers` state
2. **Key parameter**: When `answers` changes, `remember` detects the change and recalculates
3. **Lambda calculation**: Counts how many answers are not `NONE`
4. **Reactive**: Automatically updates the UI whenever answers change

## Benefits
✅ Real-time updates when selecting/changing answers
✅ Correct count when clearing all answers  
✅ Immediate UI feedback for any answer state changes
✅ Performance optimized - only recalculates when answers actually change
✅ Follows Compose best practices for derived state

## Testing
The answer count should now update correctly when:
- Selecting an answer for any question
- Changing an existing answer
- Clearing all answers
- Setting a new number of questions

## Additional Fix
Also cleaned up redundant qualifier for the back arrow icon:
```kotlin
// Before
Icon(androidx.compose.material.icons.Icons.AutoMirrored.Default.ArrowBack, ...)

// After
Icon(Icons.AutoMirrored.Default.ArrowBack, ...)
```

